package com.adobe.people.jedelson.aemrugslambda;

import com.adobe.people.jedelson.aemrugslambda.dto.GeneratorDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import scala.collection.JavaConversions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jedelson on 3/28/17.
 */
public class GeneratorsHandler extends AbstractRugsHandler<Void, List<GeneratorDTO>> {

    @Override
    protected List<GeneratorDTO> handleRequest(Void aVoid, Context context, Rugs rugs) {
        return JavaConversions.asJavaCollection(rugs.generators()).stream().map(g -> {
           return new GeneratorDTO(g);
        }).collect(Collectors.toList());
    }
}
