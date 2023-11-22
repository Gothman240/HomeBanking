const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      client: {},
      type: "",
      color: "",
    };
  },
  mounted() {
    this.toggleLoading(false);
  },
  created() {
    this.getClient();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          console.log(this.client);
        })
        .catch((err) => console.log(err));
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
    tryCreateCard() {
      this.toggleLoading(true);
      axios
        .post("/api/cards", `type=${this.type}&color=${this.color}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((response) => {
          this.showAlert(response.data, "success");
          setTimeout(() => {
            location.href = "./../web/cards.html";
          }, 2000);
        })
        .catch((err) => {
          this.toggleLoading(false);
          this.showAlert(err.response.data, "info");
        });
    },
    colorCard() {
      if (this.color === "TITANIUM") {
        return "titanium";
      }
      if (this.color === "GOLD") {
        return "gold";
      }
      if (this.color === "SILVER") {
        return "silver";
      }
    },
    secondaryColorCard() {
      if (this.color === "TITANIUM") {
        return "greydark";
      }
      if (this.color === "GOLD") {
        return "limedark";
      }
      if (this.color === "SILVER") {
        return "grey";
      }
      if (this.color == "") {
        return "bg-danger";
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
    showAlert(status, type) {
      let alerttype = type;
      if (alerttype === "success") {
        toastr.success(status);
      }

      if (alerttype === "info") {
        toastr.info(status);
      }
    },
    confirmCard() {
      if (this.type == "") {
        $("#cardType").addClass("input-error");
      }
      if (this.color == "") {
        $("#cardColor").addClass("input-error");
      }
      if (this.type && this.color) {
        this.tryCreateCard();
      }
    },
  },
  computed: {},
}).mount("#app");
