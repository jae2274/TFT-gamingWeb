//package com.tft.guide.repository;
//
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.tft.collect.entry.Participant;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static com.tft.collect.entry.QParticipant.participant;
//
//
//@Repository
//@RequiredArgsConstructor
//public class QueryRepository {
//    private final ParticipantRepository participantRepository;
//
//    public List<Participant> findByCharacterId(String characterId) {
//        BooleanExpression where = participant.units.any().characterId.eq(characterId);
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//
//        Page<Participant> page = participantRepository.findAll(where, pageRequest);
//        return page.getContent();
//    }
//
//    public List<Participant> findByCharacterId(List<String> characterIds) {
//        BooleanBuilder where = new BooleanBuilder();
//        for (String characterId : characterIds) {
//            where = where.and(participant.units.any().characterId.eq(characterId));
//        }
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//
//        Page<Participant> page = participantRepository.findAll(where, pageRequest);
//        return page.getContent();
//    }
//
//    public List<Participant> findByItemNamesIn(String itemName) {
//        BooleanExpression where = participant.units.any().itemNames.contains(itemName);
//
//        return (List<Participant>) participantRepository.findAll(where, PageRequest.of(0, 5));
//    }
//
//    public List<Participant> findByCharacterIdAndItemNamesIn(String characterId, String itemName) {
//        BooleanExpression where = participant.units.any().characterId.eq(characterId);
//        where = where.and(participant.units.any().itemNames.contains(itemName));
//
//        return (List<Participant>) participantRepository.findAll(where, PageRequest.of(0, 5));
//    }
//
//}
