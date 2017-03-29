package com.adobe.people.jedelson.aemrugslambda;

import com.adobe.people.jedelson.aemrugslambda.dto.GenerationRequestDTO;
import com.adobe.people.jedelson.aemrugslambda.dto.ValidationResultDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.param.ParameterValue;
import com.atomist.param.ParameterValues;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Seq;

import java.util.Optional;

import static scala.collection.JavaConversions.asJavaCollection;

/**
 * Created by jedelson on 3/28/17.
 */
public class ValidateHandler extends AbstractRugsHandler<GenerationRequestDTO, ValidationResultDTO> {

    private static final Logger log = LoggerFactory.getLogger(ValidateHandler.class);

    @Override
    protected ValidationResultDTO handleRequest(GenerationRequestDTO input, Context context, Rugs rugs) {
        String generatorName = (String) input.getGeneratorName();
        log.info("Using {} as generator name from {}.", generatorName, input);

        Optional<ProjectGenerator> opt = asJavaCollection(rugs.generators()).stream()
                .filter(g -> g.name().equals(input.getGeneratorName())).findFirst();
        if (opt.isPresent()) {
            ProjectGenerator generator = opt.get();
            ParameterValues paramValues = input.toParameterValues();
            if (!generator.areValid(paramValues)) {
                ValidationResultDTO result =  new ValidationResultDTO(false);
                asJavaCollection(generator.findInvalidParameterValues(paramValues)).forEach(p -> {
                    result.addInvalidParameter(p);
                });
                asJavaCollection(generator.findMissingParameters(paramValues)).forEach(p -> {
                    result.addMissingParameter(p);
                });
                return result;
            } else {
                return new ValidationResultDTO(true);
            }
        } else {
            throw new NoSuchGeneratorException(input.getGeneratorName());
        }
    }
}
