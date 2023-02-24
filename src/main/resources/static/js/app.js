


let app = new Vue({
    el: '#app',
    data: {
        // synergies:{},
        champions:[],
        jobs:[],
        affiliations:[],
        championsMap:{},
        targets:[],
        championsMapById:{},
        winners: []
    },
    async mounted(){
        const synergies = await getSynergies();
        let champions = (await getChampions()).champions;

        champions.forEach(champion=>{
            champion.traits = champion.traits.map(trait=>trait.toLowerCase());
            this.championsMapById[champion.championId] = champion;
        })

        this.jobs = synergies.jobs;
        this.affiliations = synergies.affiliations;

        for(const affiliation of this.affiliations){
            this.championsMap[affiliation.name] = {};
            for(const job of this.jobs){
                this.championsMap[affiliation.name][job.name] =
                    champions.filter(champion=>
                        champion.traits.includes(affiliation.name) && champion.traits.includes(job.name)
                    );
                // for(const champion of this.championsMap[affiliation.name][job.name])
                //     champion.cost = champion.cost>5?champion.cost/2:champion.cost

            }
        }

        this.championsMap;
    },
    methods:{
        addTargets:function (championId){
            console.log(championId);
            if(!this.targets.map(target=>target.championId).includes(championId))
                this.targets = [...this.targets, this.championsMapById[championId]];
        },
        async searchWinners(){
            const winnersRes = await callSearchWinners(this.targets);
            this.winners = winnersRes.winners;


        }
    }
})

async function getSynergies(){
    let response = await fetch("http://localhost:8080/synergies");
    let json = response.json();

    return json;
}

async function getChampions(){
    let response = await fetch("http://localhost:8080/champions");
    let json = response.json();

    return json;
}

async function callSearchWinners(targets){
    let request = {"units":[]};
    request.units = targets.map(target=>{return {"characterId":target.championId} });
    let response = await fetch("http://localhost:8080/winners" , {
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