let app = new Vue({
    el: '#app',
    data: {
        // synergies:{},
        champions: [],
        jobs: [],
        itemKeyword: '',
        affiliations: [],
        championsMap: {},
        championsForSearch: [],
        itemsForSearch: [],
        filteredItems: [],
        championsMapById: {},
        items: [],
        itemsMapById: {},
        itemsMapByName: {},
        synergiesMapById: {},
        augmentsMapById: {},
        winners: []
    },
    async mounted() {
        const promiseSynergies = getSynergies();
        const promistChampions = getChampions();
        const promiseItems = getItems();
        const promiseAugments = getAugments();


        const synergyRes = (await promiseSynergies);
        const synergies = [...synergyRes.jobs, ...synergyRes.affiliations]
        const champions = (await promistChampions).champions;
        this.items = (await promiseItems).items;

        const augments = (await promiseAugments).augments;

        champions.forEach(champion => {
            champion.traits = champion.traits.map(trait => trait.toLowerCase());
            this.championsMapById[champion.dataId] = champion;
        })
        this.filteredItems = this.items;
        this.items.forEach(item => {
            this.itemsMapById[item.dataId] = item;
        })

        this.items.forEach(item => {
            this.itemsMapByName[item.itemName] = item;
        })

        synergies.forEach(synergy => {
            this.synergiesMapById[synergy.dataId] = synergy;
        })

        augments.forEach(augment => {
            this.augmentsMapById[augment.dataId] = augment;
        })

        this.jobs = synergyRes.jobs;
        this.affiliations = synergyRes.affiliations;

        for (const affiliation of this.affiliations) {
            this.championsMap[affiliation.name] = {};
            for (const job of this.jobs) {
                this.championsMap[affiliation.name][job.name] =
                    champions.filter(champion =>
                        champion.traits.includes(affiliation.name) && champion.traits.includes(job.name)
                    )
                        .map(value => {
                            value.tier = 1;
                            return value
                        });
                // for(const champion of this.championsMap[affiliation.name][job.name])
                //     champion.cost = champion.cost>5?champion.cost/2:champion.cost

            }
        }

        this.championsMap;
    },
    updated() {
        $('[data-toggle="tooltip"]').tooltip();
    },
    methods: {
        addChampions: function (dataId) {
            console.log(dataId);
            if (!this.championsForSearch.map(target => target.dataId).includes(dataId))
                this.championsForSearch = [...this.championsForSearch, this.championsMapById[dataId]];
        },
        addItems: function (dataId) {
            console.log(dataId);
            if (!this.itemsForSearch.map(target => target.dataId).includes(dataId))
                this.itemsForSearch = [...this.itemsForSearch, this.itemsMapById[dataId]];
        },
        removeChampion(index) {

            this.championsForSearch = [...this.championsForSearch.slice(0, index), ...this.championsForSearch.slice(index + 1, this.championsForSearch.length)]
        },
        removeItem(index) {

            this.itemsForSearch = [...this.itemsForSearch.slice(0, index), ...this.itemsForSearch.slice(index + 1, this.itemsForSearch.length)]
        },
        filterItems() {
            this.filteredItems = this.items.filter(value => value.itemName.includes(this.itemKeyword))
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.championsForSearch, this.itemsForSearch);
            this.winners = winnersRes.winners;
        }
    }
})

async function getSynergies() {
    let response = await fetch("http://localhost:8080/synergies?season=8");
    let json = response.json();

    return json;
}

async function getChampions() {
    let response = await fetch("http://localhost:8080/champions?season=8");
    let json = response.json();

    return json;
}

async function getItems() {
    let response = await fetch("http://localhost:8080/items?season=8");
    let json = response.json();

    return json;
}

async function getAugments() {
    let response = await fetch("http://localhost:8080/augments?season=8");
    let json = response.json();

    return json;
}

async function callSearchWinners(champions, items) {
    let request = {
        champions: champions.map(target => {
            return {dataId: target.dataId, tier: target.tier}
        }),
        items: items.map(target => {
            return {dataId: target.dataId}
        })
    };
    let response = await fetch("http://localhost:8080/winners", {
        method: 'POST', // *GET, POST, PUT, DELETE 등
        headers: {
            'Content-Type': 'application/json',
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow', // manual, *follow, error
        referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(request), // body의 데이터 유형은 반드시 "Content-Type" 헤더와 일치해야 함
    });

    return response.json();
}