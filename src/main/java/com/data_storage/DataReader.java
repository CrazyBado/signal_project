/*DataReader Interface Implementation: Implement a class that fulfills the DataReader
interface, capable of reading data from an output file generated using the --output
file:<output_dir> argument.
Tasks:
○ Develop a method to parse data from the specified directory.
○ Ensure that the data read is accurately passed into the DataStorage for further
processing.*/

package com.data_storage;

import java.io.IOException;

public interface DataReader {
    void readData(String outputDir, DataStorage dataStorage) throws IOException;
}
