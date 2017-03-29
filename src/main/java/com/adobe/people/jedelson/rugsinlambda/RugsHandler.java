package com.adobe.people.jedelson.rugsinlambda;

import com.adobe.people.jedelson.rugsinlambda.dto.RugsDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.project.archive.Rugs;


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
