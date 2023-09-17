const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      loading: true,
      client: [],
      accounts: [],
      loans: [],
      apiLoans: [],
      accountType: "",
      totalBalance: 0,
      loanSelected: [],
      showBal: true,
      paidLoan: {
        loan_id: null,
        amount: null,
        payment: null,
        accountNumber: null,
      },
    };
  },
  created() {
    this.loadData();
    this.getAccount();
    this.getApiLoan();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
    this.showBal = JSON.parse(localStorage.getItem("eyeBalance"));
  },
  methods: {
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    loadData() {

      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.loading = false

          this.loans = response.data.loans;
        })
        .catch((err) => {
          this.loading = false
        });
    },
    getAccount() {

      axios
        .get("/api/active/accounts")
        .then((response) => {
          this.loading = false
          console.log("accounts active", response);
          this.accounts = response.data.sort((a, b) => a.id - b.id);
          this.totalBalance = this.accounts.reduce((acc, item) => {
            return acc + item.balance;
          }, 0);
        })
        .catch((err) => {
          this.loading = false
          console.log(err)
        });
    },
    getApiLoan() {

      axios.get("/api/loans").then((res) => {
        console.log(res);
        this.loading = false
        this.apiLoans = res.data;
      });
    },
    showBalance() {
      this.showBal = !this.showBal;
      localStorage.setItem("eyeBalance", JSON.stringify(this.showBal));
    },
    formatCurrency(currency) {
      const format = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      });

      return format.format(currency);
    },
    loanMonthlyCalculator(amount, payments) {
      return amount / payments;
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
    payLoan(loanSelected) {
      this.loanSelected = loanSelected;
      this.paidLoan.loan_id = loanSelected.id;
      this.paidLoan.amount = loanSelected.amount / loanSelected.payments;
      this.paidLoan.payment = this.loanSelected.payments;
    },    
    logout() {
      axios.post("/api/logout").then((res) => {
        if (res.status === 200) {
          window.location.href = "/login.html";
        }
      });
    },
    createAccount() {
      axios
        .post("/api/accounts", `accountType=${this.accountType}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((res) => {
          this.showAlert(res.data, "succsess");
          this.loadData();
          this.getAccount();
        })
        .catch((err) => {
          console.log(err.toJSON());
          this.showAlert(err.response.data, "info");
        });
    },
    sendPayLoan() {
      axios
        .post("/api/transactions/loan", this.paidLoan)
        .then((res) => {
          console.log(res);
          this.showAlert(res.data, "success");
        })
        .catch((err) => {
          console.log(err);
          this.showAlert(err.response.data, "info");
        });
    },
    filterAccount() {
      let acc = this.accounts.find(
        (account) => account.number == this.paidLoan.accountNumber
      );
      console.log(acc);
      return acc;
    },
  },
  computed: {},
}).mount("#app");
