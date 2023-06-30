const {createApp} = Vue;

createApp({
    data(){
        return {
            showSide: false,
            client: [],
            cards: [],
            debitCards: [],
            creditCards: []
        }
    },
    created(){
        this.loadData();
    },
    methods: {
        showSideBar(){
            this.showSide = !this.showSide;
        },
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response  => {
                this.client = response.data
                this.cards = response.data.cards.sort((a, b) => a.id - b.id)
                this.debitCards = this.cards.filter(card => card.type === "DEBIT")
                this.creditCards = this.cards.filter(card => card.type === "CREDIT")
                console.log(this.client)
            }).catch(err => console.log(err))
        },
        formatDate(date){
            const oldDate = date;
            const dateSplit = oldDate.split("-")

            const month = dateSplit[1];
            const year = dateSplit[0].substring(2);

            const newDate = `${year}-${month}`

            return newDate;
        },
        colorCard(color){
            if(color === "TITANIUM"){
                return "grey"
            }
            if(color === "GOLD"){
                return "yellow"
            }
            if(color === "SILVER"){
                return "grey"
            }
        },
        secondaryColorCard(color){
            if(color === "TITANIUM"){
                return "greydark"
            }
            if(color === "GOLD"){
                return "limedark"
            }
            if(color === "SILVER"){
                return "grey"
            }
        },
        logout(){
            axios.post("/api/logout")
            .then(res => {
                    window.location.href = "/login.html"
                
            })
        }
    },
    
    computed: {
       
    }
}).mount("#app")