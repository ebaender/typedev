def convolution(self, data):
    # return the data of image after convoluting process so we can check it out
    data_test = np.asmatrix(data)
    data_focus1, data_conved1 = self.convolute(
        data_test,
        self.conv1,
        self.w_conv1,
        self.thre_conv1,
        conv_step=self.step_conv1,
    )
    data_pooled1 = self.pooling(data_conved1, self.size_pooling1)

    return data_conved1, data_pooled1