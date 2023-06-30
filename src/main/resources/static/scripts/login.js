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
    };
  },
  created() {},
  methods: {
    signIn() {
      axios
        .post(
          "api/login",
          `email=${this.emailSignIn}&password=${this.passwordSignIn}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((res) => {
          window.location.href = "/web/accounts.html";
        })
        .catch((err) => console.log(err.toJSON()));
    },
    signUp() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.firstNameSignUp}&lastName=${this.lastNameSignUp}&email=${this.emailSignUp}&password=${this.passwordSignUp}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((res) => {
          axios
            .post(
              "api/login",
              `email=${this.emailSignUp}&password=${this.passwordSignUp}`,
              {
                headers: {
                  "content-type": "application/x-www-form-urlencoded",
                },
              }
            )
            .then((res) => {
              window.location.href = "/web/accounts.html";
            })
            .catch((err) => console.log(err.toJSON()));
        })
        .catch((err) => console.log(err.toJSON()));
    },
  },
  computed: {},
}).mount("#app");
