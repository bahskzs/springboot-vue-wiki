export class Tool {
  /**
   * 空校验 null或""都返回true
   */
  public static isEmpty (obj: any) {
    if ((typeof obj === 'string')) {
      return !obj || obj.replace(/\s+/g, "") === ""
    } else {
      return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
    }
  }

  /**
   * 非空校验
   */
  public static isNotEmpty (obj: any) {
    return !this.isEmpty(obj);
  }

  /**
   * 对象复制
   * @param obj
   */
  public static copy (obj: object) {
    if (Tool.isNotEmpty(obj)) {
      return JSON.parse(JSON.stringify(obj));
    }
  }


  /**
   * 使用递归将数组转为树形结构
   * 父ID属性为parent
   */
  public static array2Tree (array: any, parentId: number) {
    //如果array为空,则返回空串
    if (Tool.isEmpty(array)) {
      return [];
    }
    const result = [];
    array.forEach((item)=>{
      if(Number(item.parent) === Number(parentId)){
        //如果当前节点的parent === 传入的parent,就证明该节点是当前节点的子节点
        result.push(item);

        const children = Tool.array2Tree(array, item.id);
        if (Tool.isNotEmpty(children)) {
          item.children = children;
        }
      }
    });
    return result;
  }

  /**
   * 使用递归将将传入的节点的所有子节点查出
   *  查询传入节点的全部子节点
   */
  public static  findChildrenNode(tree:any,parentID:number) {

    const result = [];
    tree.forEach((item)=>{
      if(Number(item.parent) === Number(parentID)) {
        result.push(item.id);


        const res = Tool.findChildrenNode(tree,item.id);
        if(Tool.isNotEmpty(res)){
          result.concat(res);
        }
      }
    });

    return result;
  }




}
