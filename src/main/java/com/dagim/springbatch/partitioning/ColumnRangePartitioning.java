package com.dagim.springbatch.partitioning;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Log
public class ColumnRangePartitioning implements Partitioner {

    private int maxRowCount;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int beginRowPosition = 1;
        int targetSize = (maxRowCount - beginRowPosition)/gridSize+1;
        log.info("target size to be processed ... "+targetSize);
        Map<String, ExecutionContext> result = new HashMap<>();
        int number = 0;
        int start = beginRowPosition;
        int end = start + targetSize - 1;
        while(start <= maxRowCount){
            ExecutionContext contextValue = new ExecutionContext();
            result.put("partition"+number, contextValue);
            if(end >= maxRowCount)
                end = maxRowCount;
            contextValue.put("min-value", start);
            contextValue.put("max-value", end);
            start += targetSize;
            end += targetSize;
            number++;
            log.info("partition result ... "+result.toString());
            return result;
        }

        return null;
    }

}
