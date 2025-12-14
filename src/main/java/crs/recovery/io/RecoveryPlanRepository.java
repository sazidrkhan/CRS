/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package crs.recovery.io;

import crs.recovery.model.RecoveryPlan;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Sazid R Khan
 */
public interface RecoveryPlanRepository {
    
    List<RecoveryPlan> loadAll() throws IOException;

    void saveAll(List<RecoveryPlan> plans) throws IOException;
}
