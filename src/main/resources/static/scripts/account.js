const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      id: "",
      account: "",
      transactions: [],
      date1: null,
      date2: null

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
    deleteAccount(id){
      axios.patch(`/api/accounts/${id}`,  { headers: { "content-type": "application/x-www-form-urlencoded" } })
      .then(res => {
        console.log(res)
        
      }).catch(err => console.log(err))
    },
    postPdf() {
      console.log(this.account.id);
      console.log(typeof this.date1, this.date1);
      const date1 = moment(this.date1).format("YYYY-MM-DD HH:mm:ss.SSSSSSSSS");
      const date2 = moment(this.date2).format("YYYY-MM-DD HH:mm:ss.SSSSSSSSS");
      console.log(date1);
      axios.post(`/api/${this.account.id}/pdf?date1=${date1}&date2=${date2}`,{
        date1: date1,
        date2: date2,
      }, {
        responseType: 'arraybuffer',
      })
      .then(response => {      
        console.log(response.data)  
        const blob = new Blob([response.data], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'transactions.pdf';
        document.body.appendChild(a)
        a.click();
      })
      .catch(error => { 
        console.log(error);
      });
    },
    formattedDate(date) {      
      return dateFns.format(date, 'yyyy-MM-dd\'T\'HH:mm:ss.SSSX');
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
