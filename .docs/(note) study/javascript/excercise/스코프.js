var x = 1;

function a() {
    var x = 10;
    b();
};

function b() {
    console.log(x);
};

a();
b();