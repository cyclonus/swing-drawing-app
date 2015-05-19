package net.faraya.swing.core.layer.math;

/**
 * Created by IntelliJ IDEA.
 * User: Fabrizzio
 * Date: 10-Dec-2005
 * Time: 6:43:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Matrix2D {
  public float [][] matrix = new float [3][3];

  public Matrix2D identity(){
    /*
    (1, 0, 0),
    (0, 1, 0),
    (0, 0, 1));
    */
    /*
    matrix[0][0] = 1.00D; matrix[0][1] = 0.00D;  matrix[0][2] = 0.00D;
    matrix[1][0] = 0.00D; matrix[1][1] = 1.00D;  matrix[1][2] = 0.00D;
    matrix[2][0] = 0.00D; matrix[2][1] = 0.00D;  matrix[2][2] = 1.00D;
    */
    for(int i = 0; i != 4; i++)
      for(int j = 0; j != 4; j++){
        if(i==j){
          matrix[i][j] = 1.0F;
        }else{
          matrix[i][j] = 0.0F;
        }
      }
    return this;
  }

 private float _DET(float a1,float a2,float b1,float b2){
  return ( a1 * b2 - a2 * b1 );
 };

 private float _DET(float a1,float a2,float a3,float b1,float b2,float b3,float c1,float c2,float c3){
   return(
    a1 * (b2 * c3 - b3 * c2) -
    b1 * (a2 * c3 - a3 * c2) +
    c1 * (a2 * b3 - a3 * b2) );
 };

  public Matrix2D adjoint(){
    float a1 = matrix[0][0]; float a2 = matrix[0][1]; float a3 = matrix[0][2];
    float b1 = matrix[1][0]; float b2 = matrix[1][1]; float b3 = matrix[1][2];
    float c1 = matrix[2][0]; float c2 = matrix[2][1]; float c3 = matrix[2][2];

    matrix[0][0] =  _DET(b2, b3, c2, c3);
    matrix[0][1] = -_DET(a2, a3, c2, c3);
    matrix[0][2] =  _DET(a2, a3, b2, b3);

    matrix[1][0] = -_DET(b1, b3, c1, c3);
    matrix[1][1] =  _DET(a1, a3, c1, c3);
    matrix[1][2] = -_DET(a1, a3, b1, b3);

    matrix[2][0] =  _DET(b1, b2, c1, c2);
    matrix[2][1] = -_DET(a1, a2, c1, c2);
    matrix[2][2] =  _DET(a1, a2, b1, b2);

   return this;
  }

  public float Determinant(){

    return _DET(matrix[0][0], matrix[1][0], matrix[2][0],
                matrix[0][1], matrix[1][1], matrix[2][1],
                matrix[0][2], matrix[1][2], matrix[2][2]);
  }

  public Matrix2D scale(float factor){
    for(int i = 0; i <=2; i++)
      for(int j = 0; j <=2; j++)
        matrix[i][j] = matrix[i][j] * factor;
    return this;
  }

  public Matrix2D invert(){
   float Det = Determinant();
   if(Math.abs(Det) < 1E-5)
     identity();
   else{
     adjoint();
     scale( 1 / Det);
    }
   return this;
  }


  public Matrix2D mult(Matrix2D M2){
    Matrix2D M = new Matrix2D();
    for(int i = 0; i <=2; i++)
      for(int j = 0; j <=2; j++)
      {
       M.matrix[i][j] =
         matrix[0][j] * M2.matrix[i][0] +
         matrix[1][j] * M2.matrix[i][1] +
         matrix[2][j] * M2.matrix[i][2];
      }
    this.matrix = M.matrix;
    return this;
  }

  public Vect2D mult(Vect2D v2){
    Vect2D v = new Vect2D();
    v.x = matrix[0][0] * v2.x + matrix[1][0] * v2.y + matrix[2][0] * v.y;
    v.y = matrix[0][1] * v2.x + matrix[1][1] * v2.y + matrix[2][1] * v.y;
     //TODO: Revisar Esta imeplementacion
    return v;
  }

}
