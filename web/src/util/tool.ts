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
  public static findChildrenNode(treeNode: Array<string>, tree: Array<any>, currentId: number) {

      tree.forEach((node) => {
          if (node.id === currentId) {
              //刚好找到
              treeNode.push(node.id);

              if (Tool.isNotEmpty(node.children)) {
                  node.children.forEach((child) => {
                      Tool.findChildrenNode(treeNode, node.children, child.id);
                  });
              }

          } else {
              if (Tool.isNotEmpty(node.children)) {
                  Tool.findChildrenNode(treeNode, node.children, currentId);
              }
          }

      });


  }

    /**
     * @author: bahsk
     * @date: 2021/7/4 23:19
     * @description: 根据传入的tree,id 查出一整颗子树，并设置相应的属性
     * @params: tree, id
     * @return:
     */
    public static findChildrenTree(tree: Array<any>, currentId: number) {

        tree.forEach((node) => {
            if (node.id === currentId) {
                //刚好找到
                node.disabled = true;

                if (Tool.isNotEmpty(node.children)) {
                    node.children.forEach((child) => {
                        Tool.findChildrenTree(node.children, child.id);
                    })

                }

            } else {
                if (Tool.isNotEmpty(node.children)) {
                    Tool.findChildrenTree(node.children, currentId);
                }
            }

        });

    }


}
