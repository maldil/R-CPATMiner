import numpy as np
from package2 import example_file
import tensorflow as tf

class Test ():
    def add_arrays(self,A):
        M = [[1,2,3],[4,5,6],[7,8,9]]
        result= np.add(np.sum(np.array(M),axis=1) ,A)
        return result

    def add_one(self,M):
        result=[]
        for m in M:
            list1=[]
            for x in m:
                list1.append(x+1)
            result.append(list1)
        return result

    def add(self):
        B = self.add_one([[1,2,3],[4,5,6],[7,8,9]])
        final_result = self.add_arrays ([2,3,4])

    def foo(self):
        aa = example_file.get_a_class()
        bb = aa.boo()
        cc = np.array([1,2,3])
        dd = np.array([3,4,5])
        x1 = np.arange(9.0).reshape((3, 3))
        x2 = np.arange(3.0)
        x3 = np.add(x1, x2)
        rank_2_tensor = tf.constant([[1, 2],
                                     [3, 4],
                                     [5, 6]], dtype=tf.float16)
        mnist = tf.keras.datasets.mnist

        (x_train, y_train), (x_test, y_test) = mnist.load_data()
        x_train, x_test = x_train / 255.0, x_test / 255.0
        string_list=[[1,2,],"string"]

        myint=1

