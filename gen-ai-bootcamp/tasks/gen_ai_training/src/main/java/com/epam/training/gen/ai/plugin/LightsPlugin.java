package com.epam.training.gen.ai.plugin;

import com.epam.training.gen.ai.plugin.dto.LightModel;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LightsPlugin {

    private final Map<Integer, LightModel> lights = new HashMap<>();

    public LightsPlugin() {
        lights.put(1, new LightModel(1, "Table Lamp", false));
        lights.put(2, new LightModel(2, "Ceiling Light", false));
        lights.put(3, new LightModel(3, "Desk Lamp", false));
    }

    @DefineKernelFunction(name = "get_lights", description = "Get the list of lights")
    public List<LightModel> getLights() {
        log.info("Lights plugin getLights() was called");
        return List.copyOf(lights.values());
    }

    @DefineKernelFunction(name = "change_state", description = "Change the state of a light")
    public LightModel changeState(
            @KernelFunctionParameter(name = "id", description = "The ID of the light to change") int index,
            @KernelFunctionParameter(name = "isOn", description = "The new state of the light") boolean isOn) {
        log.info("Lights plugin changeState() was called with id: [{}] and isOn: [{}]", index, isOn);
        LightModel light = lights.get(index);
        if (light != null) {
            lights.put(index, new LightModel(light.index(), light.type(), isOn));
            return lights.get(index);
        }
        return null;
    }

}
