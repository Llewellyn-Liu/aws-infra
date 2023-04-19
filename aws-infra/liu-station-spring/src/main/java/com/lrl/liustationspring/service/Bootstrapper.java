package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.mapper.BootstrapMapper;
import org.apache.ibatis.session.SqlSession;

public class Bootstrapper {

    public static void bootstrap(){
        //Create environments
        SqlSession session = SqlConnection.getSession();
        BootstrapMapper mapper = session.getMapper(BootstrapMapper.class);
        mapper.createEnvironment();

        //Insert demo cases

    }
}
