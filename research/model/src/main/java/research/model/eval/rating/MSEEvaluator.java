/**
 * Copyright (C) 2016 LibRec
 * <p>
 * This file is part of LibRec.
 * LibRec is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * LibRec is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with LibRec. If not, see <http://www.gnu.org/licenses/>.
 */
package research.model.eval.rating;


import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import research.model.eval.AbstractRecommenderEvaluator;
import research.model.recommend.item.ContextKeyValueEntry;
import research.model.recommend.item.KeyValue;
import research.model.recommend.item.RecommendedList;

import java.util.Iterator;
import java.util.List;

/**
 * MSE: mean square error
 *
 * @author Keqiang Wang
 */
public class MSEEvaluator extends AbstractRecommenderEvaluator<DoubleCollection> {

    public double evaluate(RecommendedList groundTruthList, RecommendedList recommendedList) {
        if (groundTruthList.size() == 0) {
            return 0.0;
        }

        double mse = 0.0;
        int testSize = 0;

        Iterator<ContextKeyValueEntry> groundTruthIter = groundTruthList.iterator();
        Iterator<ContextKeyValueEntry> recommendedEntryIter = recommendedList.iterator();

        while (groundTruthIter.hasNext()) {

            if (recommendedEntryIter.hasNext()) {

                ContextKeyValueEntry groundEntry = groundTruthIter.next();
                ContextKeyValueEntry recommendedEntry = recommendedEntryIter.next();

                if (groundEntry.getContextIdx() == recommendedEntry.getContextIdx()
                        && groundEntry.getKey() == recommendedEntry.getKey()) {

                    double realRating = groundEntry.getValue();
                    double predictRating = recommendedEntry.getValue();
                    mse += Math.pow(realRating - predictRating, 2);

                    testSize++;

                } else {
                    throw new IndexOutOfBoundsException("index of recommendedList does not equal testMatrix index");
                }

            } else {
                throw new IndexOutOfBoundsException("index cardinality of recommendedList does not equal testMatrix index cardinality");
            }
        }

        return testSize > 0 ? mse / testSize : 0.0d;
    }

    @Override
    public  double evaluate(DoubleCollection ratingSetInTest, List<KeyValue> recommendedList){
        if(ratingSetInTest.size() == 0){
            return 0.0d;
        }
        double mse = 0.0d;
        Iterator<Double> realRatingIter = ratingSetInTest.iterator();
        for(KeyValue<String,Double> keyValue: recommendedList){
            double predictRating = keyValue.getValue();
            double realRating = realRatingIter.next();
            mse += Math.pow(predictRating - realRating,2);
        }
        return mse;
    }
}