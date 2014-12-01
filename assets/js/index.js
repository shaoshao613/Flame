$( document ).ready(function() {
                    
                    // Set canvas drawing surface
                    var space = document.getElementById("surface");
                    var surface = space.getContext("2d");
                    surface.scale(1,1);
                    
                    // Set Particles
                    var particles = [];
                    var particle_count =150;
                    for(var i = 0; i < particle_count; i++) {
                    particles.push(new particle());
                    }
                    var time = 0;
                    var count=0;
                    // Set wrapper and canvas items size
                    var canvasWidth=screen.width;
                    var canvasHeight=0.9*screen.availHeight;
                    var image=document.getElementById('img');
                    
                    //在(0,50)处绘制图片
                    
                    
                    $(".wrapper").css({width:canvasWidth,height:canvasHeight});
                    $("#surface").css({width:canvasWidth,height:canvasHeight});
                    
                    // shim layer with setTimeout fallback from Paul Irish
                    window.requestAnimFrame = (function(){
                                               return  window.requestAnimationFrame       ||
                                               window.webkitRequestAnimationFrame ||
                                               window.mozRequestAnimationFrame    ||
                                               function( callback ){
                                               window.setTimeout(callback, 600 / 60);
                                               };
                                               })();
                    
                    function particle() {
                    
                    this.speed = {x: -6+Math.random()*12, y: -5+Math.random()*5};
                    canvasWidth = (document.getElementById("surface").width);
                    canvasHeight= (document.getElementById("surface").height);
                    this.location = {x: canvasWidth/4, y: (canvasHeight)};
                    
                    this.radius = .5+Math.random();
                    
                    this.life = 10+Math.random()*10*7;
                    this.death = this.life;
                    
                    this.r = 255;
                    this.g = Math.round(Math.random()*155);
                    this.b = 0;
                    }
                    
                    function ParticleAnimation(){
                    
                    //surface.fillRect(0, 0, canvasWidth, canvasHeight);
                    surface.globalCompositeOperation = "lighter";
                    //surface.drawImage(image,0,0);
                    count++;
                    
                    for(var i = 0; i < particles.length; i++)
                    {
                    var p = particles[i];
                    
                    surface.beginPath();
                    
                    p.opacity = Math.round(p.death/p.life*100)/100
                    var gradient = surface.createRadialGradient(p.location.x, p.location.y, 0, p.location.x, p.location.y, p.radius);
                    gradient.addColorStop(0, "rgba(0, "+p.g+", "+p.b+", "+p.opacity+")");
                    //	gradient.addColorStop(0.5, "rgba("+p.r+", "+p.g+", "+p.b+", "+p.opacity+")");
                    gradient.addColorStop(1, "rgba("+p.r+", "+p.g+", "+p.b+", 0)");
                    surface.fillStyle = gradient;
                    surface.arc(p.location.x, p.location.y, p.radius, Math.PI*2, false);
                    
                    
                    surface.fill();
                    if(count<100){
                    p.death-=3-count/50;}
                    p.radius+=2;
                    p.location.x += (p.speed.x)*2;
                    p.location.y += (p.speed.y)*2;
                    
                    //regenerate particles
                    if(p.death < 0 || p.radius < 0){
                    //a brand new particle replacing the dead one
                    particles[i] = new particle();
                    }
                    }
                    
                    
                    
                    
                    requestAnimFrame(ParticleAnimation);
                    
                    }
                    ParticleAnimation();
                    
                    });