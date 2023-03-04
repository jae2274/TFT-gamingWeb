let app = new Vue({
    el: '#app',
    data: {
        augments: [],
        champions: [],
        jobs: [],
        itemKeyword: '',
        championKeyword: '',
        augmentKeyword: '',
        affiliations: [],
        championsMap: {},
        championsForSearch: [],
        itemsForSearch: [],
        augmentsForSearch: [],
        championsMapById: {},
        items: [],
        champions: [],
        itemsMapById: {},
        itemsMapByName: {},
        synergiesMapById: {},
        augmentsMapById: {},
        winners: [],
        currentOffset: 0,
    },
    computed: {
        filteredItems() {
            return this.itemKeyword ? this.items.filter(value => value.itemName.includes(this.itemKeyword)) : [];
        },
        filteredChampions() {
            return this.championKeyword ? this.champions.filter(value => value.championName.includes(this.championKeyword)) : [];
        },
        filteredAugments() {
            return this.augmentKeyword ? this.augments.filter(value => value.augmentName.includes(this.augmentKeyword)) : [];
        }
    },
    async mounted() {
        const promiseSynergies = getSynergies();
        const promistChampions = getChampions();
        const promiseItems = getItems();
        const promiseAugments = getAugments();


        const synergyRes = (await promiseSynergies);
        const synergies = [...synergyRes.jobs, ...synergyRes.affiliations]
        this.champions = (await promistChampions).champions;
        this.items = (await promiseItems).items;

        this.augments = (await promiseAugments).augments;

        this.champions.forEach(champion => {
            champion.traits = champion.traits.map(trait => trait.toLowerCase());
            this.championsMapById[champion.dataId] = champion;
        })
        this.items.forEach(item => {
            this.itemsMapById[item.dataId] = item;
        })

        this.items.forEach(item => {
            this.itemsMapByName[item.itemName] = item;
        })

        synergies.forEach(synergy => {
            this.synergiesMapById[synergy.dataId] = synergy;
        })

        this.augments.forEach(augment => {
            this.augmentsMapById[augment.dataId] = augment;
        })

        this.jobs = synergyRes.jobs;
        this.affiliations = synergyRes.affiliations;

        for (const affiliation of this.affiliations) {
            this.championsMap[affiliation.name] = {};
            for (const job of this.jobs) {
                this.championsMap[affiliation.name][job.name] =
                    this.champions.filter(champion =>
                        champion.traits.includes(affiliation.name) && champion.traits.includes(job.name)
                    )
                        .map(value => {
                            value.tier = 1;
                            return value
                        });

            }
        }

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
        addAugments: function (dataId) {
            console.log(dataId);
            if (!this.augmentsForSearch.map(target => target.dataId).includes(dataId))
                this.augmentsForSearch = [...this.augmentsForSearch, this.augmentsMapById[dataId]];
        },
        removeChampion(index) {

            this.championsForSearch = [...this.championsForSearch.slice(0, index), ...this.championsForSearch.slice(index + 1, this.championsForSearch.length)]
        },
        removeItem(index) {
            this.itemsForSearch = [...this.itemsForSearch.slice(0, index), ...this.itemsForSearch.slice(index + 1, this.itemsForSearch.length)]
        },
        removeAugment(index) {
            this.augmentsForSearch = [...this.augmentsForSearch.slice(0, index), ...this.augmentsForSearch.slice(index + 1, this.augmentsForSearch.length)]
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.championsForSearch, this.itemsForSearch, this.augmentsForSearch, 0, 5);
            this.winners = winnersRes.winners;
            this.currentOffset = winnersRes.winners.length;
        },
        async replaceWinner(index) {
            const response = await callSearchWinners(this.championsForSearch, this.itemsForSearch, this.currentOffset, 1)
            this.winners = [...this.winners.slice(0, index), ...this.winners.slice(index + 1, this.winners.length), ...response.winners]
            this.currentOffset++
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

async function callSearchWinners(champions, items, augments, offset, size) {
    let request = {
        champions: champions.map(target => {
            return {dataId: target.dataId, tier: target.tier}
        }),
        items: items.map(target => {
            return {dataId: target.dataId}
        }),
        augments: augments.map(target => {
            return {dataId: target.dataId}
        }),
        offset,
        size
    };


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