const { createApp } = Vue;

createApp({
  data() {
    return {
      showSide: false,
      loading: true,
      client: [],
      cards: [],
      debitCards: [],
      creditCards: [],
      selectedCard:{},
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
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.getCards();
          this.client = response.data;
          this.loading = false
          console.log(response.data.cards[0].thruDate);
        })
        .catch((err) => console.log(err));
    },
    getCards() {
      axios
        .get("/api/active/cards")
        .then((response) => {
          this.cards = response.data.sort((a, b) => a.id - b.id);
          if (this.cards != null) {
            this.debitCards = this.cards.filter(
              (card) => card.type === "DEBIT"
            );
            this.creditCards = this.cards.filter(
              (card) => card.type === "CREDIT"
            );
          }          
        })
        .catch((error) => {
          console.log(error);
        });
    },
    formatDate(date) {
      const oldDate = date;
      const dateSplit = oldDate.split("-");

      const month = dateSplit[1];
      const year = dateSplit[0].substring(2);

      const newDate = `${year}-${month}`;

      return newDate;
    },
    colorCard(color) {
      if (color === "TITANIUM") {
        return "grey";
      }
      if (color === "GOLD") {
        return "yellow";
      }
      if (color === "SILVER") {
        return "grey";
      }
    },
    secondaryColorCard(color) {
      if (color === "TITANIUM") {
        return "greydark";
      }
      if (color === "GOLD") {
        return "limedark";
      }
      if (color === "SILVER") {
        return "grey";
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
    deleteCard(id) {
      axios
        .patch(
          `/api/cards/${id}`,
          { isActive: false },
          {
            headers: { "Content-Type": "application/json" },
          }
        )
        .then((res) => {
          console.log(res.data);
          this.showAlert(res.data, "success");
          setTimeout(() => {
            this.loadData();
            this.getCards();
          }, 1000);
        })
        .catch((err) => {
          console.log(err);
          this.showAlert(err.response.data, "info");
        });
    },
    selectedCards(card){
      console.log(card)
      this.selectedCard = card;
      
      setTimeout(() => {
        $('#myModal').modal('show');
      }, 100); // Puedes ajustar el tiempo según tus necesidades

      console.log(this.selectedCard)
    },
    logout() {
      axios.post("/api/logout").then((res) => {
        window.location.href = "./../index.html";
      });
    },
    cardAlertExpired(date){
      console.log("fecha de expiracion de cartas " + date)
      let currentDate = new Date();
      let year = currentDate.getFullYear();
      let month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
      let days = ('0' + (currentDate.getDate() + 1)).slice(-2);
      let thruDate = date;
      console.log(Math.abs(month - thruDate.slice(5,7)) == 2)
      if(year == thruDate.slice(0, 4)){
        if(Math.abs(month - thruDate.slice(5,7)) == 2){
          return "quedan 2 meses"
        }
      }

    }
  },

  computed: {
    
  },
}).mount("#app");
