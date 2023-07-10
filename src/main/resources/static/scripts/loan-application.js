const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      accounts: [],
      loans: [],
      loansApi: [],
      loanSelected: {},
      loanSelectedPayments: [],
      formData: {
        id_loan: null,
        amount: 0,
        payments: null,
        numberAccountDestiny: "",
      },
    };
  },
  created() {
    this.loadData();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
    this.getLoans();
  },
  mounted() {
    
  },
  methods: {
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    loadData() {
      axios
        .get("http://localhost:8080/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.accounts = response.data.accounts;
          this.loans = response.data.loans;
        })
        .catch((err) => console.log(err));
    },
    getLoans() {
      axios
        .get("http://localhost:8080/api/loans")
        .then((response) => {
          this.loansApi = response.data;
          console.log(response);
          console.log(this.loans);
        })
        .catch((error) => console.log(error));
    },
    showForm(id) {
      this.loanSelected = this.loansApi.filter((item) => item.id == id);
      this.loanSelectedPayments = this.loanSelected.payments;
      this.formData.id_loan = id;
    },
    formatCurrency(currency) {
      const format = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      });

      return format.format(currency);
    },
    logout() {
      axios.post("/api/logout").then((res) => {
        if (res.status === 200) {
          window.location.href = "/login.html";
        }
      });
    },
    submit() {
      axios
        .post("/api/loans", this.formData, {
          headers: { "Content-Type": "application/json" },
        })
        .then((res) => {
          console.log("creado");
          this.showAlert(res.data, "success");
          setTimeout(() => {
            window.location.href = "./../web/accounts.html"
          }, 2000);
        })
        .catch((err) => {
          this.showAlert(err.response.data, "info");
        });
    },
    showAlert(status, type) {
      let alerttype = type;
      if (alerttype === "success") {
        toastr.success(status);
      }

      if (alerttype === "info") {
        toastr.info(status);
      }
    },
    totalLoan(currency){
      let maxAmount=0;      
      if(!isNaN(this.loanSelected)){ 
        maxAmount = this.loanSelected.maxAmount
      }
  
      if(currency > maxAmount){
        let total = currency + (20 / 100) * currency
        return this.formatCurrency(total);
      }
    },
    limitMaxValue(value){
      if(this.formData.amount > value){
        this.formData.amount = value;
      }
    },
    maxAmountInput(id){
      const selectLoan = this.loansApi.filter(item => item.id == id)
      let maxAmount = selectLoan.maxAmount
      console.log('select loan: ' + maxAmount);
        return maxAmount 
  
      
     }
  },
  computed: {
   
 },
}).mount("#app");