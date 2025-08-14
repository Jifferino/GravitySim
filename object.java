import java.awt.geom.Ellipse2D;

public class object extends main {
    
    public float m;
    public float a;
    public vector pos;
    public Ellipse2D self;
    //public float x; 
    //public float y;
    public float radius;
    public vector vel = new vector(0, 0);
    public vector Accel = new vector(0,0);
    
    private static final float G = (float) 6.67e-11; //scaled up by a factor of a 1*e12


    public object(float m, vector pos, float radius){
        
        this.m = m;
        this.pos = pos;
        this.radius = radius;

        this.self = new Ellipse2D.Float(this.pos.getX(), this.pos.getY(), radius, radius);
        float randx = (float) (Math.random() - 0.5) * 1.5f;
        float randy = (float) (Math.random() - 0.5) * 1.5f;
        vel.x = randx;
        vel.y = randy;
    }

    public float getMass(){
        return this.m;
    }
    public float getX(){
        return this.pos.x;
    }
    public float getY(){
        return this.pos.y;
    }

    public vector calculateNetForce(){
        vector Force = new vector(0,0);
        float dist = 0;

        for(object obj : gamePanel.Objects){
            dist = (float) this.pos.getDist(obj.pos);
            if(this.self != obj.self){
                float F = (G*obj.getMass()*this.getMass())/(dist*dist);
                
                float Fx = F * this.pos.displacement(obj.pos).normalization().getX();//normalization is the equivalent of cos(theta) in this case
                float Fy = F * this.pos.displacement(obj.pos).normalization().getY();//normalization is the equivalent of sin(theta) in this case

                Force.x += Fx;
                Force.y += Fy;
            }
        }

        return Force;
    }

    public vector calculateNetAccel(){
        Accel.x = calculateNetForce().x/this.getMass();
        Accel.y = calculateNetForce().y/this.getMass();

        return Accel;
    }

    public vector getVel(){
        vel.x += (float) (calculateNetAccel().x);
        vel.y += (float) (calculateNetAccel().y);
        /**if(vel.magnitude() > 90){
            vel.x += vel.displacement(this.pos).normalization().x * 90;
            vel.y += vel.displacement(this.pos).normalization().y * 90; //Dont understand why this normalization is here       
        }
        else{
            vel.x += (float) (calculateNetAccel().x);
            vel.y += (float) (calculateNetAccel().y); 
        }**/

        return vel;
    }

    public vector updatePos(double timeInt){

        this.pos.x += (float) (getVel().x * timeInt);
        this.pos.y += (float) (getVel().y * timeInt);
    
        this.self.setFrame(this.pos.x - this.radius/2, this.pos.y - this.radius/2, this.radius, this.radius);
        // System.out.print("X:");
        // System.out.println(pos.x);
        // System.out.print("Y:");
        // System.out.println(pos.y);
        // System.out.println();

        System.out.println("Pre Boundary");
        System.out.println(vel.x);
        System.out.println(vel.y);
        return this.pos;
    }

    public void updateConstraint(){

        vector center = new vector(gamePanel.screenWidth/2, gamePanel.screenHeight/2);
        // System.out.println("Conditional does not work");
        double radius = 1000;
        
        if(this.pos.displacement(center).magnitude() > radius - this.radius){

            this.pos.x = (float) (center.x - (this.pos.displacement(center).normalization().x * (radius-this.radius)));
            this.pos.y = (float) (center.y - (this.pos.displacement(center).normalization().y * (radius-this.radius)));

            this.vel = vel.multiply(-0.9f);

            // System.out.println("BOUNDARY HIT!!");
            // System.out.println(vel.x);
            // System.out.println(vel.y);
        }

    }

    public void collisions(){
        for(object obj : gamePanel.Objects){
            if(this.self != obj.self){
                vector displacementBetween = new vector(obj.pos.displacement(this.pos).x, obj.pos.displacement(this.pos).y);
                double distance = displacementBetween.magnitude();
                if(distance < obj.radius + this.radius){
                    float combinedRadius = (float) (obj.radius + this.radius);
                    float delta = (float) (combinedRadius - distance);
                    
                    this.pos = this.pos.addVector((displacementBetween.displacement(this.pos).normalization().multiply((this.radius/combinedRadius * delta))));
                    obj.pos = obj.pos.addVector((displacementBetween.displacement(obj.pos).normalization().multiply((-obj.radius/combinedRadius * delta))));
                }
            }
        }
    }
    
}
