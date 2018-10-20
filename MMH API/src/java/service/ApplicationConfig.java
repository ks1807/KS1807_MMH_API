/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Jonathon
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(service.MoodScoreFacadeREST.class);
        resources.add(service.MusicTrackFacadeREST.class);
        resources.add(service.PlayListFacadeREST.class);
        resources.add(service.TracksInPlayListFacadeREST.class);
        resources.add(service.UserAccountFacadeREST.class);
        resources.add(service.UserDiaryFacadeREST.class);
        resources.add(service.UserMoodFacadeREST.class);
        resources.add(service.UserSettingsFacadeREST.class);
    }
    
}
