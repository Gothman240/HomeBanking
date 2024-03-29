const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      loading: true,
      paymentToPush: null,
      loanDTO: {
        name: "",
        maxAmount: null,
        payments: [],
        loanPercentage: null,
      },
      apiLoans: [],
    };
  },
  mounted(){
    this.toggleLoading(false)
  },
  created() {
    this.getLoans();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
    this.showBal = JSON.parse(localStorage.getItem("eyeBalance"));
  },
  methods: {
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    getLoans() {
      axios
        .get("/api/loans")
        .then((response) => {
          this.loading = false;
          this.apiLoans = response.data;
          console.log(this.apiLoans);
        })
        .catch((err) => {
          this.loading = false;
          console.log(err);
        });
    },
    addArray() {
      if (!isNaN(this.paymentToPush)) {
        if (this.paymentToPush > 1) {
          let boolean = this.loanDTO.payments.some(
            (payment) => payment == this.paymentToPush
          );
          if (boolean == false) {
            this.loanDTO.payments.push(this.paymentToPush);
            this.paymentToPush = null;
            this.loanDTO.payments.sort((a, b) => a - b);
          }
        }
      }
    },
    removeFromArray(payment) {
      const index = this.loanDTO.payments.indexOf(payment);
      if (index !== -1) {
        this.loanDTO.payments.splice(index, 1);
      }
    },
    createLoan() {
        this.toggleLoading(true)
      axios
        .post("/api/loans/admin", this.loanDTO)
        .then((response) => {
          this.showAlert(response.data, "success");
        })
        .catch((err) => {
          console.log(err);
          this.showAlert(err.response.data, "info");
          this.toggleLoading(false)
        });
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
    toggleLoading(value) {
      if (value === true) {
        $(".spinner-border.spinner-border-sm.me-2").show("swing");
        $("button.btn-isLoading").prop("disabled", true);
      } else {
        $(".spinner-border.spinner-border-sm.me-2").hide("swing");
        $("button.btn-isLoading").prop("disabled", false);
      }
    },
    logout() {
      axios.post("/api/logout").then((res) => {
        if (res.status === 200) {
          window.location.href = "/index.html";
        }
      });
    },
  },
  computed: {},
}).mount("#app");
