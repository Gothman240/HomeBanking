const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      client: [],
      accounts: [],
      account: [],
      formData: {
        originNumber: null,
        destinyNumber: null,
        toOtherDestinyNumber: null,
        amount: undefined,
        description: "",
      },
    };
  },
  created() {
    this.getClient();
    this.getActiveAccounts();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
  },
  methods: {
    getClient() {
      let param = new URLSearchParams(location.search);
      let num = param.get("num");
      console.log(num);
      this.formData.originNumber = num;
      console.log(this.formData.originNumber);
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
        })
        .catch((err) => console.log(err));
    },
    getActiveAccounts(){
      axios.get("/api/active/accounts")
      .then((response) => {
        this.accounts = response.data;
      })
      .catch((err) => console.log(err))
    },
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    logout() {
      axios
        .post("/api/logout")
        .then((res) => {
          if (res.status === 200) {
            window.location.href = "./../index.html";
          }
        })
        .catch((err) => console.log(err.toJSON()));
    },
    formatCurrency(currency) {
      const format = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      });

      return format.format(currency);
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
    submitForm() {

      if(this.formData.toOtherDestinyNumber != null){
        this.formData.destinyNumber = "VIN-" + this.formData.toOtherDestinyNumber
      }

      const queryParams = new URLSearchParams({
        originNumber: this.formData.originNumber,
        destinyNumber: this.formData.destinyNumber,
        amount: this.formData.amount,
        description: this.formData.description,
      });

      axios
        .post(
          "/api/transactions",
          `amount=${this.formData.amount}&description=${this.formData.description}&originNumber=${this.formData.originNumber}&destinyNumber=${this.formData.destinyNumber}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {            
            window.location.href = `./../web/summary-transfer.html?${queryParams.toString()}`;
          
        })
        .catch((err) => {
          
          this.showAlert(err.response.data, "info");
        });
    },
    clearToAccount() {
      if (this.formData.originNumber == this.formData.destinyNumber) {
        this.formData.destinyNumber = null;
      }
    },
    formValidation(){
      if(this.formData.originNumber == null){
        $("#originNumber").addClass("input-error");
      }else{
        $("#originNumber").addClass("input-success");
      }
      if(this.formData.destinyNumber == null){
        $("#destinyNumber").addClass("input-error");
      }else{
        $("#destinyNumber").addClass("input-success");
      }
      if(this.formData.toOtherDestinyNumber == null){
        $("#destinyNumberOther").addClass("input-error");
      }else{
        $("#destinyNumberOther").addClass("input-success");
      }
      if(this.formData.destinyNumber == null){
        $("#destinyNumberOther").addClass("input-error");
      }else{
        $("#destinyNumberOther").addClass("input-success");
      }
      if(this.formData.amount == undefined || this.formData.amount.isNaN || this.formData.amount < 1){
        $("#amount").addClass("input-error");
      }else{
        $("#amount").addClass("input-success");
      }
      if(this.formData.description == ""){
        $("#description").addClass("input-error");
      }else{
        $("#description").addClass("input-success");
      }

      if(this.formData.originNumber && (this.formData.destinyNumber || this.formData.toOtherDestinyNumber) && this.formData.amount > 1 && this.formData.description != ""){
        $("#modalConfirm").modal("show")
      }

    }
  },
  computed: {
    showAccount() {
      return this.accounts.filter(
        (account) => account.number === this.formData.originNumber
      );
    },
    showBalance() {
      if (this.currentStep == 2) {
        this.account = this.accounts.filter(
          (account) => account.balance == this.formData.originNumber
        );
      }
    },   
  },
}).mount("#app");
