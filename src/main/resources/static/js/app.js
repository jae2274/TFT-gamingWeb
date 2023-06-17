Vue.component('championImg', {
    // JavaScript는 camelCase
    props: ['cost', 'imageUrl', 'traits', 'tooltipText'],
    computed: {
        costClass() {
            return 'cost-' + this.cost;
        },
        costText() {
            return '$' + this.cost;
        }
    },
    template:
        `
    <div class="tft-champion tft-champion--42" :class="costClass" v-tooltip="tooltipText">
      <img :src="imageUrl">
      <span class="cost">{{costText}}</span>
    </div>
    `
})

Vue.component('itemImg', {
    props: ['imageUrl', 'tooltipText'],
    computed: {},
    template: `
    <div class="targets"style="position: relative;display: inline-block;">
        <div class="search-item tft-item tft-item--42" style="display: inline-block;"
        v-tooltip="tooltipText">
            <img v-bind:src="imageUrl" alt=""
                 style="float: left;display:inline-block;height: 100%;">
        </div>
    </div>
    `
})

Vue.component('winnerTemplate', {
    props: ['synergies', 'augments', 'champions', 'index'],
    methods: {
        replaceWinner(index) {
            this.$emit('winner-replaced', index)
        },
        setHover(synergy, isHover) {
            synergy.isHover = isHover;
            // console.log(this.synergies.filter(value => value.dataId == synergy.dataId)[0].isHover);

            for (const champion of this.champions) {
                if (isHover) {
                    const isSelected = champion.traits.includes(synergy.name);
                    champion.isSelected = isSelected;
                } else {
                    champion.isSelected = false;
                }
            }
        }
    },
    template: `
        <div class="profile__match-history-v2__item placement-4 " data-region="KR"
         data-game-id="6032745922" style="display: inline-block">

            <div class="traits" style="width: 765px;display: flex; text-align: center;color:white;">
                <div class="trait" style="width: 70px;margin:5px; "
                     v-for="synergy in synergies" 
                     v-on:mouseover="setHover(synergy, true)"
                     v-on:mouseout="setHover(synergy, false)"
                     v-tooltip.top-end="synergy.desc+'<br/>'+synergy.stats.join('<br/>')"
                     >
                    <img v-bind:src="synergy.imageUrl" style="width:48px;">
                         <div>{{synergy.name}}<br/>{{synergy.numUnits}}</div>
                </div>
            </div>
            <div style="display: flex;">
                <div class="augments" style="padding-left: 10px;padding-top: 17px;">
                    <div class="augment" v-for="augment in augments">
                        <img v-bind:src="augment?augment.imageUrl:'/image/question-mark.jpg'"
                             v-tooltip="augment?augment.name:''" style="width:24px;height:24px;">
                    </div>
                </div>
    
                <div class="units" style="width:1000px;">
                    <div class="unit" :class="{selected: champion.isSelected}" v-for="champion in champions"
                         style="width: 90px;height: 100px;">
                        <img v-bind:src=" '//cdn.lolchess.gg/images/tft/stars/cost' + champion.cost + '_stars'+champion.tier + '.png' "
                             class="stars">
                        <div>
                            <champion-img
                                    v-bind:cost="champion.cost"
                                    v-bind:image-url="champion.imageUrl"
                                    v-bind:traits="champion.traits"
                                            v-bind:tooltip-text="champion.tooltipText"
                                    v-on:click.native="addChampions(champion.dataId)"
                            />
                        </div>
                        <ul class="items" style="padding-top: 3px;">
                            <img v-for="item in champion.items"
                                 v-bind:src="item?item.imageUrl:'/image/question-mark.jpg'" class="item"
                                 v-tooltip="item?item.name:''"
                                 style="width:30px;height: 30px;">
                        </ul>
                    </div>
                </div>
            </div>
    
            <div v-on:click="replaceWinner(index)"
                 style="height:100%;width:60px;background-color:sandybrown;position:absolute;top:0;right:0;cursor: pointer;">
            </div>
        </div>
    `
})

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
            list: [],
            searchWord: '',
            requests: [],
            mapById: {},
            // synergiesMapById: {},
        },
        winners: [],
        currentOffset: 0,
        stats: {},
        statsMap: {},
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
        filteredSynergies() {
            return this.synergyObj.searchWord ? this.synergyObj.list.filter(value => value.name.includes(this.synergyObj.searchWord)) : [];
        },
    },
    async mounted() {
        const promiseSynergies = getSynergies();
        const promistChampions = getChampions();
        const promiseItems = getItems();
        const promiseAugments = getAugments();
        const promiseStats = getStats();


        const synergyRes = (await promiseSynergies);
        this.synergyObj.list = [...synergyRes.jobs, ...synergyRes.affiliations]
        this.championObj.list = (await promistChampions).champions;
        this.itemObj.list = (await promiseItems).items;
        this.augmentObj.list = (await promiseAugments).augments;
        this.statsMap = (await promiseStats)

        const statsMap = this.statsMap;


        this.championObj.list.forEach(champion => {
            champion.traits = champion.traits.map(trait => trait.toLowerCase());
            this.championObj.mapById[champion.dataId] = champion;
        });
        this.itemObj.list.forEach(item => {
            this.itemObj.mapById[item.dataId] = item;
            this.itemObj.mapByName[item.name] = item;
        });


        this.championObj.list.forEach(champion => {
            const stats = statsMap.champions[champion.dataId];
            if (stats) {
                champion.tooltipText =
                    `${champion.name}<br/>` +
                    champion.traits.join('&nbsp;&nbsp;') + '<br/><br/>평균등수<br/>'
                    + getAvgPlacementByTiers(stats.tiers)
                    + "<br/>"
                    + getTootipForChampionItems(stats.itemsSortedByCount, this.itemObj.mapById)
                // + "<br/>"
                // + getTootipForChampionItems(stats.itemsSortedByPlacement, this.itemObj.mapById)

                champion.averagePlacement = (stats.totalPlacement / stats.totalCount).toFixed(2);
            }
        });
        this.itemObj.list.forEach(item => {
            const stats = statsMap.items[item.dataId];
            if (stats) {
                item.tooltipText =
                    `${item.name}<br/>` +
                    `평균등수: ${(stats.totalPlacement / stats.totalCount).toFixed(2)}<br/>`
                    + "<br/>"
                    + getTootipForChampionItems(stats.championsSortedByCount, this.championObj.mapById)
            }
        });


        this.championObj.list = this.championObj.list.sort((prev, next) => prev.averagePlacement - next.averagePlacement)


        this.synergyObj.list.forEach(synergy => {
            this.synergyObj.mapById[synergy.dataId] = synergy;
            synergy.tier = 1;

            const stats = statsMap.synergies[synergy.dataId];
            if (stats) {
                synergy.tooltipText = synergy.desc + '<br/><br/>평균등수<br/>'
                    + getAvgPlacementByTiers(stats.tiers)

                synergy.averagePlacement = (stats.totalPlacement / stats.totalCount).toFixed(2);
            }
        })
        this.synergyObj.list = this.synergyObj.list.sort((prev, next) => prev.averagePlacement - next.averagePlacement)

        this.augmentObj.list.forEach(augment => {
            this.augmentObj.mapById[augment.dataId] = augment;

            const stats = statsMap.augments[augment.dataId];
            if (stats)
                augment.averagePlacement = (stats.totalPlacement / stats.totalCount).toFixed(2);
        })
        this.augmentObj.list = this.augmentObj.list.sort((prev, next) => prev.averagePlacement - next.averagePlacement)

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
                            value.itemCount = 0;
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
        addSynergies: function (dataId) {
            console.log(dataId);
            if (!this.synergyObj.requests.map(target => target.dataId).includes(dataId))
                this.synergyObj.requests = [...this.synergyObj.requests, this.synergyObj.mapById[dataId]];
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
        removeSynergy(index) {
            this.synergyObj.requests = [...this.synergyObj.requests.slice(0, index), ...this.synergyObj.requests.slice(index + 1, this.synergyObj.requests.length)]
        },
        async searchWinners() {
            const winnersRes = await callSearchWinners(this.championObj.requests, this.itemObj.requests, this.augmentObj.requests, this.synergyObj.requests, 0, 5);
            this.winners = this.winnersOf(winnersRes);
            // this.winners = winnersRes.winners;
            this.currentOffset = winnersRes.winners.length;
        },
        async replaceWinner(index) {
            const response = await callSearchWinners(this.championObj.requests, this.itemObj.requests, this.augmentObj.requests, this.synergyObj.requests, this.currentOffset, 1)

            this.winners.splice(index, 1);
            for (const winner of this.winnersOf(response)) {
                this.winners.push(winner)
            }
            this.currentOffset++
        },
        winnersOf(winnersRes) {
            return winnersRes.winners
                .map(winnerRes => {
                    return {
                        synergies: winnerRes.traits
                            .filter(trait => trait.tierCurrent != 0)
                            .sort((prev, next) => {
                                const diff = next.tierCurrent - prev.tierCurrent;
                                return diff != 0 ? diff : next.numUnits - prev.numUnits;
                            })
                            .map(trait => {
                                let synergy = Object.assign({}, this.synergyObj.mapById[trait.name]);
                                synergy.numUnits = trait.numUnits;
                                return synergy;
                            }),
                        augments: winnerRes.augments
                            .map(augment => this.augmentObj.mapById[augment]),
                        champions: winnerRes.units
                            .map(unit => {
                                let champion = Object.assign({}, this.championObj.mapById[unit.characterId]);

                                champion.tier = unit.tier;

                                const items = unit.itemNames.map(itemName => this.itemObj.mapById[itemName])
                                champion.items = items;
                                champion.isSelected = false;

                                const stats = this.statsMap.champions[champion.dataId];
                                // champion.tooltipText = champion.traits.join('&nbsp;&nbsp;') + '<br/><br/>평균등수<br/>'
                                //     + getAvgPlacementByTiers(stats.tiers)
                                //         .map(tier => `${tier[0]}: ${tier[1]}`)
                                //         .join('<br/>');

                                return champion;
                            })
                    }
                })
        }
    }
})

function getAvgPlacementByTiers(tiers) {
    return tiers
        .filter(value => value.key != 0)
        .sort((prev, next) => prev.key - next.key)
        .map(value => {

            return [value.key, (value.totalPlacement / value.totalCount).toFixed(2)];
        })
        .map(tier => `${tier[0]}: ${tier[1]}`)
        .join('<br/>')
}

function getTootipForChampionItems(items, itemMapById) {
    return items
        .filter(value => itemMapById[value.key])
        .map(value => {
            const imageUrl = itemMapById[value.key].imageUrl
            return `
                    <div>
                    <img src="${imageUrl}" style="height: 30px;">
                    <span>${(value.totalPlacement / value.totalCount).toFixed(2)}</span>
                    <span>${value.totalCount}</span>
                    </div>
        `
        })
        .join("")
}


async function getSynergies() {
    let response = await fetch("/synergies?season=9");
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

async function getChampions() {
    let response = await fetch("/champions?season=9");
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

async function getItems() {
    let response = await fetch("/items?season=9");
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

async function getAugments() {
    let response = await fetch("/augments?season=9");
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

async function getStats() {
    let response = await fetch("/stats?season=9");
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

async function callSearchWinners(champions, items, augments, synergies, offset, size) {
    let requests = {
        champions: champions.map(target => {
            return {dataId: target.dataId, tier: target.tier, itemCount: target.itemCount}
        }),
        items: items.map(target => {
            return {dataId: target.dataId}
        }),
        augments: augments.map(target => {
            return {dataId: target.dataId}
        }),
        synergies: synergies.map(target => {
            return {dataId: target.dataId, tier: target.tier}
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
    let json = await response.json();

    if (response.status == 200 && json.success)
        return json.data;
    else {
        alert(json.message)
    }
}

