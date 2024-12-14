package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;


@Slf4j
public class WeatherPlugin {

    private static final Random random = new Random();
    private static final String[] weatherOptions;
    private static final List<String> supportedLocations;

    static {
        weatherOptions = new String[]{
                "sunny",
                "cloudy",
                "rainy",
                "snowy",
                "stormy"
        };

        supportedLocations = List.of(
                "london",
                "paris",
                "berlin",
                "moscow",
                "tokyo",
                "new york",
                "los angeles",
                "sydney",
                "beijing",
                "cairo",
                "podgorica"
        );
    }

    @DefineKernelFunction(name = "getWeather", description = "Get the current weather for a location.")
    public String getWeather(@KernelFunctionParameter(name = "location") String location) {
        log.info("Weather plugin was called with location: [{}]", location);
        if (!supportedLocations.contains(location.toLowerCase())) {
            return "Sorry, I don't have information about the weather in " + capitalize(location) + ".";
        }

        int temperature = random.nextInt(15) + 10;
        String condition = weatherOptions[random.nextInt(weatherOptions.length)];
        return "The weather in " + capitalize(location) + " is " + condition + " with a temperature of " + temperature + "°C.";
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
