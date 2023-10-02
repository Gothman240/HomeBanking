const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      client: {},
      dateNow: new Date(),
      originNumber: null,
      destinyNumber: null,
      amount: undefined,
      description: "",
    };
  },
  created() {
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));

    const queryParams = new URLSearchParams(window.location.search);

    const originNumber = queryParams.get("originNumber");
    const destinyNumber = queryParams.get("destinyNumber");
    const amount = queryParams.get("amount");
    const description = queryParams.get("description");

    this.originNumber = originNumber;
    this.destinyNumber = destinyNumber;
    this.amount = amount;
    this.description = description;

    const sound = document.createElement("audio");
    sound.id = "audio";
    sound.src = "./../assets/sound/beep-6-96243.mp3";
    sound.type = "audio/mp3";
    document.body.appendChild(sound);
    document.getElementById("audio").play();
  },
  methods: {
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
    goHome(){
        this.originNumber= null,
        this.destinyNumber= null,
        this.amount= undefined,
        this.description= "",
        window.location.href = "./../web/accounts.html"
    },
    formatDate(date) {
        const format = {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false
        };
        const parsedDate = new Date(date);
        return parsedDate.toLocaleDateString("en-US", format);
      },
  },
  computed: {},
}).mount("#app");
