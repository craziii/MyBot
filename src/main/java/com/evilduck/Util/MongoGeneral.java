package com.evilduck.Util;

import com.evilduck.Repository.BigDickRepository;
import com.evilduck.Repository.CommandDetailRepository;
import com.evilduck.Repository.MemberRepository;

public class MongoGeneral {

    private final BigDickRepository bigDickRepository;
    private final CommandDetailRepository commandDetailRepository;
    private final MemberRepository memberRepository;

    public MongoGeneral(final BigDickRepository bigDickRepository,
                        final CommandDetailRepository commandDetailRepository,
                        final MemberRepository memberRepository) {
        this.bigDickRepository = bigDickRepository;
        this.commandDetailRepository = commandDetailRepository;
        this.memberRepository = memberRepository;
    }

    public void clearAllRepositories() {
        bigDickRepository.deleteAll();
        commandDetailRepository.deleteAll();
        memberRepository.deleteAll();
    }

}
