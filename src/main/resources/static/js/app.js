let app = new Vue({
    el: '#app',
    data: {
        // synergies:{},
        champions: [],
        jobs: [],
        affiliations: [],
        championsMap: {},
        targets: [],
        championsMapById: {},
        itemsMapById: {},
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
        const items = (await promiseItems).items;
        const augments = (await promiseAugments).augments;

        champions.forEach(champion => {
            champion.traits = champion.traits.map(trait => trait.toLowerCase());
            this.championsMapById[champion.dataId] = champion;
        })

        items.forEach(item => {
            this.itemsMapById[item.dataId] = item;
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
                    );
                // for(const champion of this.championsMap[affiliation.name][job.name])
                //     champion.cost = champion.cost>5?champion.cost/2:champion.cost

            }
        }

        this.championsMap;
    },
    methods: {
        addChampions: function (dataId) {
            console.log(dataId);
            if (!this.targets.map(target => target.dataId).includes(dataId))
                this.targets = [...this.targets, this.championsMapById[dataId]];
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.targets);
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

async function callSearchWinners(targets) {
    let request = {"units": []};
    request.units = targets.map(target => {
        return {"characterId": target.dataId}
    });
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