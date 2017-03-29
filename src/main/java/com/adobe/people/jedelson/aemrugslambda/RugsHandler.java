package com.adobe.people.jedelson.aemrugslambda;

import com.adobe.people.jedelson.aemrugslambda.dto.RugDTO;
import com.adobe.people.jedelson.aemrugslambda.dto.RugsDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.project.archive.Rugs;
import scala.collection.JavaConversions;


import static scala.collection.JavaConversions.asJavaCollection;

public class RugsHandler extends AbstractRugsHandler<Void, RugsDTO> {

    @Override
    protected RugsDTO handleRequest(Void aVoid, Context context, Rugs rugs) {
        RugsDTO result = new RugsDTO();
        asJavaCollection(rugs.generators()).stream().forEach(g -> {
           result.addGenerator(g);
        });
        asJavaCollection(rugs.editors()).stream().forEach(g -> {
            result.addEditor(g);
        });
        return result;
    }
}
