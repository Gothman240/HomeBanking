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
          if (res.status === 200) {
            window.location.href = "/web/accounts.html";
          }
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
          if (res.status === 201) {
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
                if (res.status === 200) {
                  window.location.href = "/web/accounts.html";
                }
              })
              .catch((err) => console.log(err.toJSON()));
          }
        });
    },
  },
  computed: {},
}).mount("#app");
