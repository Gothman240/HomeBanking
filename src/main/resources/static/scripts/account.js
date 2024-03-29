const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      client: [],
      loading: true,
      id: "",
      account: "",
      transactions: [],
      date1: null,
      date2: null,
      activeAlert: false,
    };
  },
  mounted(){
    this.toggleLoading(false)
  },
  created() {
    this.loadData();
    this.getClient();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
  },
  methods: {
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.loading = false;
          this.client = response.data;
        })
        .catch((err) => console.log(err));
    },
    loadData() {
      let param = new URLSearchParams(location.search);
      this.id = parseInt(param.get("id"));
      axios
        .get(`/api/accounts/${this.id}`)
        .then((response) => {
          this.loading = false;
          this.transactions = response.data.transactions;
          this.account = response.data;
          this.transactions.sort((a, b) => b.id - a.id);
        })
        .catch((err) => console.log(err));
    },
    formatDate(date) {
      const format = {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
      };
      const parsedDate = new Date(date);
      return parsedDate.toLocaleDateString("en-US", format);
    },
    formatCurrency(currency) {
      const format = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      });

      return format.format(currency);
    },
    deleteAccount(id) {
      axios
        .patch(`/api/accounts/${id}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((res) => {
          this.showAlert(res.data, "success");
          window.location.href="./../web/accounts.html"
        })
        .catch((err) => {
          this.showAlert(err.response.data, "info")
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
    PDFmodal(){
      $("#modalPDF").modal("show")
    },
    postPdf() {
      this.toggleLoading(true)
      const date1 = moment(this.date1).format("YYYY-MM-DD HH:mm:ss.SSSSSSSSS");
      const date2 = moment(this.date2).format("YYYY-MM-DD HH:mm:ss.SSSSSSSSS");      
      axios
        .post(
          `/api/pdf/${this.account.id}/?date1=${date1}&date2=${date2}`,
          {
            date1: date1,
            date2: date2,
          },
          {
            responseType: "arraybuffer",
          }
        )
        .then((response) => {
          $("#modalPDF").modal("hide")
          this.toggleLoading(false)
          this.activeAlert=false
          const blob = new Blob([response.data], { type: "application/pdf" });
          const url = URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = url;
          a.download = "transactions.pdf";
          document.body.appendChild(a);
          a.click();
        })
        .catch((error) => {
          this.activeAlert = true
          this.toggleLoading(false);          
        });
    },
    formattedDate(date) {
      return dateFns.format(date, "yyyy-MM-dd'T'HH:mm:ss.SSSX");
    },
    toggleLoading(value) {
      if (value === true) {
        $("span.material-icons-loading").hide()
        $(".spinner-border.spinner-border-sm.me-2").show("swing");
        $("button.btn-isLoading").prop("disabled", true);
      } else {
        $(".spinner-border.spinner-border-sm.me-2").hide("swing");
        $("button.btn-isLoading").prop("disabled", false);
      }
    },
    showInfoPdf(value) {
      this.activeAlert = value
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
