let app = new Vue({
    el: '#app',
    data: {
        augmentObj: {
            list: [],
            searchWord: '',
            requests: [],
            mapById: {},
        },
        itemObj: {
            list: [],
            searchWord: '',
            requests: [],
            mapById: {},
            mapByName: {},

        },
        championObj: {
            list: [],
            searchWord: '',
            mapBySynergies: {},
            requests: [],
            mapById: {},
        },
        synergyObj: {
            jobs: [],
            affiliations: [],
        },
        synergiesMapById: {},
        winners: [],
        currentOffset: 0,
    },
    computed: {
        filteredAugments() {
            return this.augmentObj.searchWord ? this.augmentObj.list.filter(value => value.name.includes(this.augmentObj.searchWord)) : [];
        },
        filteredItems() {
            return this.itemObj.searchWord ? this.itemObj.list.filter(value => value.name.includes(this.itemObj.searchWord)) : [];
        },
        filteredChampions() {
            return this.championObj.searchWord ? this.championObj.list.filter(value => value.name.includes(this.championObj.searchWord)) : [];
        },
    },
    async mounted() {
        const promiseSynergies = getSynergies();
        const promistChampions = getChampions();
        const promiseItems = getItems();
        const promiseAugments = getAugments();


        const synergyRes = (await promiseSynergies);
        const synergies = [...synergyRes.jobs, ...synergyRes.affiliations]
        this.championObj.list = (await promistChampions).champions;
        this.itemObj.list = (await promiseItems).items;

        this.augmentObj.list = (await promiseAugments).augments;

        this.championObj.list.forEach(champion => {
            champion.traits = champion.traits.map(trait => trait.toLowerCase());
            this.championObj.mapById[champion.dataId] = champion;
        })
        this.itemObj.list.forEach(item => {
            this.itemObj.mapById[item.dataId] = item;
        })

        this.itemObj.list.forEach(item => {
            this.itemObj.mapByName[item.name] = item;
        })

        synergies.forEach(synergy => {
            this.synergiesMapById[synergy.dataId] = synergy;
        })

        this.augmentObj.list.forEach(augment => {
            this.augmentObj.mapById[augment.dataId] = augment;
        })

        this.synergyObj.jobs = synergyRes.jobs;
        this.synergyObj.affiliations = synergyRes.affiliations;

        for (const affiliation of this.synergyObj.affiliations) {
            this.championObj.mapBySynergies[affiliation.name] = {};
            for (const job of this.synergyObj.jobs) {
                this.championObj.mapBySynergies[affiliation.name][job.name] =
                    this.championObj.list.filter(champion =>
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
            if (!this.championObj.requests.map(target => target.dataId).includes(dataId))
                this.championObj.requests = [...this.championObj.requests, this.championObj.mapById[dataId]];
            this.championObj.requests = this.championObj.requests.sort((prev, next) => prev.cost - next.cost);
        },
        addItems: function (dataId) {
            console.log(dataId);
            if (!this.itemObj.requests.map(target => target.dataId).includes(dataId))
                this.itemObj.requests = [...this.itemObj.requests, this.itemObj.mapById[dataId]];
        },
        addAugments: function (dataId) {
            console.log(dataId);
            if (!this.augmentObj.requests.map(target => target.dataId).includes(dataId))
                this.augmentObj.requests = [...this.augmentObj.requests, this.augmentObj.mapById[dataId]];
        },
        removeChampion(index) {

            this.championObj.requests = [...this.championObj.requests.slice(0, index), ...this.championObj.requests.slice(index + 1, this.championObj.requests.length)]
        },
        removeItem(index) {
            this.itemObj.requests = [...this.itemObj.requests.slice(0, index), ...this.itemObj.requests.slice(index + 1, this.itemObj.requests.length)]
        },
        removeAugment(index) {
            this.augmentObj.requests = [...this.augmentObj.requests.slice(0, index), ...this.augmentObj.requests.slice(index + 1, this.augmentObj.requests.length)]
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.championObj.requests, this.itemObj.requests, this.augmentObj.requests, 0, 5);
            this.winners = winnersRes.winners;
            this.currentOffset = winnersRes.winners.length;
        },
        async replaceWinner(index) {
            const response = await callSearchWinners(this.championObj.requests, this.itemObj.requests, this.currentOffset, 1)
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
    let requests = {
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
        body: JSON.stringify(requests), // body의 데이터 유형은 반드시 "Content-Type" 헤더와 일치해야 함
    });

    return response.json();
}