import java.util.Random;

public class Main {
    public static Double[][] InitializeMarks(){
        Random rand = new Random();
        Double[][] grades = new  Double[5][5];
        for(int i = 0; i < grades.length; i++){
            for(int j = 0; j < grades[i].length; j++){
                grades[i][j] = rand.nextDouble() * 10;
            }
        }
        return grades;
    }

    public static Double StudentAverage (Double[] grades){
        Double sum = 0.0;
        for(int i = 0; i < grades.length; i++){
            sum += grades[i];
        }
        return sum / grades.length;
    }

    public static void EvalStudentAverage (Double[] grades){
        Double avg = StudentAverage (grades);
        if(avg>8.0){
            System.out.println("Good grades | Average: " + String.format("%.2f", avg));
        } else if (avg>5.0){
            System.out.println("Average grades | Average: " + String.format("%.2f", avg));
        } else {
            System.out.println("Failing | Average: " + String.format("%.2f", avg));
        }
    }

    public static void main(String[] args) {
        Double[][] grades = InitializeMarks();
        for(int i = 0; i < grades.length; i++){
            EvalStudentAverage (grades[i]);
        }

    }
}

