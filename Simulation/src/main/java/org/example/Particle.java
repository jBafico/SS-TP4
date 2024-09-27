package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Particle {
    private double position;
    private double velocity;
    private double mass;
    private double force;


    public Particle cloneParticle(){
        return new Particle(position,velocity,mass,force);
    }
}

/*CI
r(t0)=1
v(t0)=-1*100/2=-50
m=70
a=m/F
 */
