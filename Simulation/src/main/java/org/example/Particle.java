package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class Particle {
    private double position;
    private double velocity;
}

/*CI
r(t0)=1
v(t0)=-1*100/2=-50
m=70
a=m/F
 */
