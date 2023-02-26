package com.tft.guide.controller.response

import com.tft.guide.entity.Synergy
import com.tft.guide.entity.SynergyType

data class SynergiesRes(
        val affiliations: List<SynergyRes>? = null,
        val jobs: List<SynergyRes>? = null,
) {
    companion object {
        fun of(synergies: List<Synergy>): SynergiesRes {
            val affiliationSynergies: List<Synergy> = synergies.filter { it.type == SynergyType.AFFILIATION }
            val jobsSynergies: List<Synergy> = synergies.filter { it.type == SynergyType.JOB }

            return SynergiesRes(
                    affiliations = SynergyRes.listOf(affiliationSynergies),
                    jobs = SynergyRes.listOf(jobsSynergies),
            )
        }
    }

    data class SynergyRes(
            val name: String,
            val type: SynergyType,
            val desc: String,
            val stats: List<String>,
            val imageUrl: String,
            val dataId: String,
    ) {
        companion object {
            fun listOf(synergies: List<Synergy>): List<SynergyRes> {
                return synergies.map {
                    SynergyRes(
                            name = it.name,
                            type = it.type,
                            desc = it.desc,
                            stats = it.stats,
                            imageUrl = it.imageUrl,
                            dataId = it.dataId,
                    )
                }
            }
        }

    }


}