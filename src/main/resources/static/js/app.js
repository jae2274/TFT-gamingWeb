let app = new Vue({
    el: '#app',
    data: {
        // synergies:{},
        champions: [],
        jobs: [],
        itemKeyword: '',
        championKeyword: '',
        affiliations: [],
        championsMap: {},
        championsForSearch: [],
        filteredChampions: [],
        itemsForSearch: [],
        filteredItems: [],
        championsMapById: {},
        items: [],
        champions: [],
        itemsMapById: {},
        itemsMapByName: {},
        synergiesMapById: {},
        augmentsMapById: {},
        winners: [],
        replaceMatchIds: new Set(),
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
        this.champions = champions;
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
            this.championsForSearch = this.championsForSearch.sort((prev, next) => prev.cost - next.cost);
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
        filterChampions() {
            this.filteredChampions = this.champions.filter(value => value.championName.includes(this.championKeyword))
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.championsForSearch, this.itemsForSearch);
            this.winners = winnersRes.winners;
            this.replaceMatchIdSet = new Set();
        },
        async replaceWinner(index) {
            this.winners.map(value => value.match_id).forEach(value => this.replaceMatchIdSet.add(value))

            const response = await callSearchWinners(this.championsForSearch, this.itemsForSearch, this.replaceMatchIdSet)
            this.winners = [...this.winners.slice(0, index), ...this.winners.slice(index + 1, this.winners.length), ...response.winners]
        }
    }
})

async function getSynergies() {
    let response = await fetch("/synergies?season=8");
    let json = response.json();

    return json;
}

async function getChampions() {
    let response = await fetch("/champions?season=8");
    let json = response.json();

    return json;
}

async function getItems() {
    let response = await fetch("/items?season=8");
    let json = response.json();

    return json;
}

async function getAugments() {
    let response = await fetch("/augments?season=8");
    let json = response.json();

    return json;
}

async function callSearchWinners(champions, items, replaceMatchIdSet) {
    let request = {
        champions: champions.map(target => {
            return {dataId: target.dataId, tier: target.tier}
        }),
        items: items.map(target => {
            return {dataId: target.dataId}
        }),
    };

    if (replaceMatchIdSet)
        request.replaceMatchIds = Array.from(replaceMatchIdSet);

    let response = await fetch("/winners", {
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