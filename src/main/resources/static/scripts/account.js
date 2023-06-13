const { createApp } = Vue;

createApp({
  data() {
    return {
      show: false,
      id: "",
      transactions: [],
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    showSideBar() {
      this.show = !this.show;
    },
    loadData() {
      let param = new URLSearchParams(location.search);
      this.id = parseInt(param.get("id"));
      axios
        .get(`http://localhost:8080/api/accounts/${this.id}`)
        .then((response) => {
          this.transactions = response.data.transactions;
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

  },
  computed: {},
}).mount("#app");
