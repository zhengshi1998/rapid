package engine.racedetectionengine.OSR.POBuild;

import java.util.ArrayList;

// This class solves Range Minima Query using segment tree
public class RangeMinimaQuery {
    public ArrayList<Integer> inThreadIdArray;
    public int[] segmentTree; // tree node = [left idx of nums, right idx of nums, minVal]
    int numsSize;

    public RangeMinimaQuery(){}

    // Given the partial order built in POBuildReverse, initialize the segment tree
    // There is an edge from fromNodes[i] to toNodes[i].
    public RangeMinimaQuery(ArrayList<Integer> fromNodes, ArrayList<Integer> toNodes){
//        this.nums = nums;
        this.inThreadIdArray = fromNodes;
        this.buildTree(toNodes);
    }

    // Compute the size of segment tree
    public void buildUpArray(ArrayList<Integer> nums){
        this.numsSize = nums.size();

        int exp = 1, limit = 2 * this.numsSize - 1;

        while(limit > exp - 1){
            exp = exp * 2;
        }
        int treeSize = exp - 1;

        this.segmentTree = new int[treeSize];
    }

    // Entry method for building segment tree
    public void buildTree(ArrayList<Integer> nums){
        if(nums.size() > 0) {
            this.buildUpArray(nums);
            this.buildTree(0, 0, this.numsSize - 1, nums);
        }
    }

    // Build the tree for segment [numsLeft, numsRight] in nums array.
    // This method returns the value we should save in this.segmentTree[treeLoc].
    public int buildTree(int treeLoc, int numsLeft, int numsRight, ArrayList<Integer> nums){
        int val;
        if(numsLeft == numsRight){
            val = nums.get(numsLeft);
            this.segmentTree[treeLoc] = val;
            return val;
        }

        int mid = (numsLeft + numsRight) / 2;

        int left = this.buildTree(2 * treeLoc + 1, numsLeft, mid, nums);
        int right = this.buildTree(2 * treeLoc + 2, mid + 1, numsRight, nums);

        val = Math.min(left, right);
        this.segmentTree[treeLoc] = val;
        return val;
    }

    // Given a query [left, right], return the min value in this range
    public int rangeQueryMin(int left, int right){
        if(left < 0 || right >= this.numsSize || left > right) return -1;
        else return rangeQueryMin(0, 0, this.numsSize - 1, left, right);
    }


    // recursive query method
    public int rangeQueryMin(int treeLoc, int curNodeLeft, int curNodeRight, int queryLeft, int queryRight){
        if(queryLeft <= curNodeLeft && queryRight >= curNodeRight) {
            // curNode is in query range, return node value
            return this.segmentTree[treeLoc];
        }
        else if(queryLeft > curNodeRight || queryRight < curNodeLeft) return Integer.MAX_VALUE; // out of range
        else {
            int nodeMid = (curNodeLeft + curNodeRight) / 2;
            int leftVal = rangeQueryMin(2 * treeLoc + 1, curNodeLeft, nodeMid, queryLeft, queryRight);
            int rightVal = rangeQueryMin(2 * treeLoc + 2, nodeMid + 1, curNodeRight, queryLeft, queryRight);
            return Math.min(leftVal, rightVal);
        }
    }

    // find the largest index idx in nums, s.t. nums[idx] >= val
    public int searchIndexLeft(int val){
        if(this.inThreadIdArray.get(0) < val) return -1;
        int size = this.inThreadIdArray.size();
        int left = 0, right = size - 1;
        int mid = -1;

        while(left < right){
            mid = (left + right) / 2;
            int midVal = this.inThreadIdArray.get(mid);

            if(midVal == val) return mid;
            else if(midVal > val){
                if(mid + 1 < size){
                    if(this.inThreadIdArray.get(mid + 1) >= val){
                        left = mid + 1;
                    } else {
                        return mid;
                    }
                } else {
                    return size - 1;
                }
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    // find the smallest index idx in nums, s.t. nums[idx] <= val
    public int searchIndexRight(int val){
        if(this.inThreadIdArray.get(this.inThreadIdArray.size()-1) > val) return this.inThreadIdArray.size();

        int left = 0, right = this.inThreadIdArray.size() - 1;
        int mid = -1;

        while(left < right){
            mid = (left + right) / 2;
            int midVal = inThreadIdArray.get(mid);

            if(midVal == val) return mid;
            else if(midVal > val){
                left = mid + 1;
            } else {
                if(mid - 1 >= 0){
                    if(this.inThreadIdArray.get(mid - 1) <= val){
                        right = mid - 1;
                    } else {
                        return mid;
                    }
                } else {
                    return 0;
                }
            }
        }

        return left;
    }

    // Note that the input leftInThId, rightInThId are different from the index
    // of this.segmentTree index.
    // This methods maps a query on in-thread Id into segmentTree index and performs the query.
    public int getMinWithRange(int leftInThId, int rightInThId) {
        if(this.inThreadIdArray.size() == 0) return -1;

        int right = searchIndexLeft(leftInThId);
        int left = searchIndexRight(rightInThId);

        if(left < 0 || right >= this.inThreadIdArray.size() || left > right) return -1;

        return this.rangeQueryMin(left, right);
    }
}
