class config{
 final String[] serverList = { "A", "B", "C", "D" };



}

class Idk extends config{ 
    Idk(){
        while(true){
        for(int i =0 ; i <server)
        Thread T1 = new Thread(()->{
        boolean isSuccesful = false;
        // We will hit  our server 
        // If successful then we will change it true 

            try{Thread.sleep(500);}catch(Exception e){};
        });
    }}

}



class RoundRobin extends config{
    // String req;
    static int Bouncer = 0;

    RoundRobin(String req) {
        // this.req = req;
        forwarder(req);
    }

    public boolean forwarder(String req) {
        try {
            // forwarwarding request to server accoring to the boucer

            // ServerA s = new ServerA(re)

            if (Bouncer < serverList.length) {
                Bouncer++;
            } else {
                Bouncer = 0;
            }
            return true;
        } catch (Exception e) {
            // inform someone
            return false;
        }
    }

}