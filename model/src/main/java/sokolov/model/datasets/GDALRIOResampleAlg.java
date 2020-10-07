package sokolov.model.datasets;

public enum GDALRIOResampleAlg {
    /*! Nearest neighbour */                            GRIORA_NearestNeighbour(0),
    /*! Bilinear (2x2 kernel) */                        GRIORA_Bilinear(1),
    /*! Cubic Convolution Approximation (4x4 kernel) */ GRIORA_Cubic(2),
    /*! Cubic B-Spline Approximation (4x4 kernel) */    GRIORA_CubicSpline(3),
    /*! Lanczos windowed sinc interpolation (6x6 kernel) */ GRIORA_Lanczos(4),
    /*! Average */                                      GRIORA_Average(5),
    /*! Mode (selects the value which appears most often of all the sampled points) */
    GRIORA_Mode(6),
    /*! Gauss blurring */                               GRIORA_Gauss(7);

    public int value;

    GDALRIOResampleAlg(int value){
        this.value = value;
    }
}
