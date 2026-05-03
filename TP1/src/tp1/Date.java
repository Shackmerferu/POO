package tp1;

import  java.util.GregorianCalendar;
public class Date {
    public static void main(String[] args){
        GregorianCalendar fecha= new GregorianCalendar();
        int d,m,a,h,mi,s;
        d= fecha.get(GregorianCalendar.DAY_OF_MONTH);
        m=fecha.get(GregorianCalendar.MONTH);
        a=fecha.get(GregorianCalendar.YEAR);
        h=fecha.get(GregorianCalendar.HOUR_OF_DAY);
        mi=fecha.get(GregorianCalendar.MINUTE);
        s=fecha.get(GregorianCalendar.SECOND);

        System.out.println("Fecha actual:"+d+"/"+m+"/"+a);
        System.out.println("Hora actual:"+h+":"+mi+":"+s);
    }
}
