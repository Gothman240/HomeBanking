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
    downloadPDF() {
      const date1 = this.formattedDate(this.date1)
      console.log("asd ", typeof date1, date1)
      const date2 = this.formattedDate(this.date2)
      axios.post(`/api/transactions/pdf?date1=${date1}&date2=${date2}`, {
        headers: {
          'Content-Type': 'application/json',
        },
        responseType: 'arraybuffer',
      })
      .then(response => {        
        const pdfBlob = new Blob([response.data], { type: 'application/pdf' });
        const url = URL.createObjectURL(pdfBlob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'transactions.pdf');
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch(error => { 
        console.log(error)
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
