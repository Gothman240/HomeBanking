const {createApp} = Vue;

createApp({
    data(){
        return {
            showSide: false,
            client: [],
            accounts: [],
            loans: [],
            totalBalance: 0,
            showBal: false,
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
            axios.get("http://localhost:8080/api/clients/1")
            .then(response  => {
                this.client = response.data;
                this.accounts = response.data.accounts;
                this.loans = response.data.loans
                this.accounts.sort((a, b) => a.id - b.id);
                this.totalBalance = this.accounts.reduce((acc, item) => {
                    return acc + item.balance
                }, 0)
                
                console.log(this.client.loans)
            })
            .catch(err => console.log(err))
        },
        showBalance(){
            this.showBal = !this.showBal;
            console.log(this.showBal)
        }
    },
    computed: {
       
    }
}).mount("#app")