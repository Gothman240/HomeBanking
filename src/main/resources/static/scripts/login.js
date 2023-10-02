const { createApp } = Vue;

createApp({
  data() {
    return {
      emailSignIn: "",
      passwordSignIn: "",
      firstNameSignUp: "",
      lastNameSignUp: "",
      emailSignUp: "",
      passwordSignUp: "",
      isLoading: false,
      errorMessage: "",
    };
  },
  mounted () {
    this.toggleLoading(false);
  },
  methods: {
    async signIn(user, password) {
      this.toggleLoading(true);
      try {
        const response = await axios.post(
          "/api/login",
          `email=${user}&password=${password}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        );
        location.href="./../web/accounts.html"
      } catch (error) {
        this.toggleLoading(false)
        this.handleError(error);
      }
    },
    async signUp() {
      this.toggleLoading(false);
      try {
        const response = await axios.post(
          "/api/clients",
          `firstName=${this.firstNameSignUp}&lastName=${this.lastNameSignUp}&email=${this.emailSignUp}&password=${this.passwordSignUp}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        );
        this.signIn(this.emailSignUp, this.passwordSignUp)
      } catch (error) {
        toggleLoading(false)
      }
    },
    toggleLoading(value) {
      if (value === true) {
        $(".spinner-border.spinner-border-sm.me-2").show("swing");
        $("button.btn").prop("disabled", true);
      } else {
        $(".spinner-border.spinner-border-sm.me-2").hide("swing");
        $("button.btn").prop("disabled", false);
        $("div .alert.alert-danger").hide()
      }      
    },
    handleError(error) {
      $("div .alert.alert-danger").show("swing")
      this.errorMessage = "Login failed. Please check your credentials and try again.";
    },
  },
}).mount("#app");