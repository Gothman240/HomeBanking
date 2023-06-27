const { createApp } = Vue;

createApp({
    data(){
        return {
            showSide: false,
            client: [],
            accounts: [],
            loans: [],
            totalBalance: 0,
            showBal: true,
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
                this.client = response.data;
                this.accounts = response.data.accounts;
                this.loans = response.data.loans
                this.accounts.sort((a, b) => a.id - b.id);
                this.totalBalance = this.accounts.reduce((acc, item) => {
                    return acc + item.balance
                }, 0)
                
            })
            .catch(err => console.log(err))
        },
        showBalance(){
            this.showBal = !this.showBal;
            console.log(this.showBal)
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
    computed: {
       
    }
}).mount("#app")