package com.epam.training.gen.ai.controller;

import com.azure.core.annotation.FormParam;
import com.epam.training.gen.ai.dto.VectorPoint;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/vector/text")
@RequiredArgsConstructor
public class VectorController {

    public final SimpleVectorActions simpleVectorActions;

    @PostMapping("/process")
    public ResponseEntity<?> processText(@FormParam("text") String text) throws ExecutionException, InterruptedException {
        // Process and save text
        simpleVectorActions.processAndSaveText(text);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchText(@FormParam("text") String text) throws ExecutionException, InterruptedException {
        // Search for similar vectors
        List<Points.ScoredPoint> points = simpleVectorActions.search(text);
        List<VectorPoint> pointList = points.stream().map(point -> {
            JsonWithInt.Value info = point.getPayloadOrThrow("info");
            return new VectorPoint(info.getStringValue(), point.getScore());
        }).toList();
        return ResponseEntity.ok(pointList);
    }

}
