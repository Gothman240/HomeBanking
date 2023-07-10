const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      id: "",
      account: "",
      transactions: [],
    };
  },
  created() {
    this.loadData();
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
  },
  methods: {
    showSideBar() {
      this.showSide = !this.showSide;
      localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    loadData() {
      let param = new URLSearchParams(location.search);
      this.id = parseInt(param.get("id"));
      axios
        .get(`http://localhost:8080/api/accounts/${this.id}`)
        .then((response) => {
          this.transactions = response.data.transactions;
          this.account = response.data
          this.transactions.sort((a, b) => b.id - a.id)          
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
        hour12: false
      };
      const parsedDate = new Date(date);
      return parsedDate.toLocaleDateString("en-US", format);
    },
    formatCurrency(currency){
      const format = new Intl.NumberFormat('en-US', {
          style: 'currency',
          currency: 'USD',
      })

      return format.format(currency)
  },
    logout(){
      axios.post("/api/logout")
      .then(res => {
          if(res.status === 200){
              window.location.href = "/login.html"
          }
      })
  }
  },
  computed: {},
}).mount("#app");
