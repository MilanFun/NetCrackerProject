import {Component, OnInit, ViewChild} from '@angular/core';
import {jqxTreeGridComponent} from 'jqwidgets-ng/jqxtreegrid';
import {jqxMenuComponent} from 'jqwidgets-ng/jqxmenu';
import {TagService} from "../../services/tag-service/tag.service";
import {Tag} from "../../model/entity/tag";

@Component({
  selector: 'app-tag-tree',
  templateUrl: './tag-tree.component.html',
  styleUrls: ['./tag-tree.component.less']
})

export class TagTreeComponent implements OnInit {
  @ViewChild('TreeGrid') treeGrid: jqxTreeGridComponent;
  @ViewChild('myTreeGrid') myTreeGrid: jqxTreeGridComponent;
  @ViewChild('myMenu') myMenu: jqxMenuComponent;

  constructor(private tagService: TagService) {
  }

  getWidth(): any {
    if (document.body.offsetWidth < 260) {
      return '90%';
    }
    return 250;
  }

  tags: any[] = null;

  employees: any[] = [
    {
      id: 2,
      title: "Andrew",
      parentId: "Fuller",
      expanded: "true",
      children: [
        {
          id: 8,
          title: "Laura",
          parentId: "Callahan"
        }
      ]
    }
  ];

  ngOnInit(): void {
    let allTags: Tag[];

    this.tagService.getAll().subscribe(tags => {
      allTags = tags
      this.tags = allTags.filter(tag => tag.parentId === 0);

      for (let tag of this.tags) {
        tag.children = allTags.filter(x => x.parentId === tag.id);
        tag.expanded = false;
        this.setExpandedChild(allTags, tag.children);
      }
      this.dataAdapter = new jqx.dataAdapter(this.getSource());
      this.dataAdapter.localData = this.tags;
    });
    
    
  }

  setExpandedChild(allTags: any[], tags: any): void {
    if(!tags.length)
      return;
    for (let node of tags) {
      node.children = allTags.filter(x => x.parentId === node.id);
      node.expanded = false;
      this.setExpandedChild(allTags, node.children);
    }
  }

  getSource(): any {
    return {
      dataType: "json",
      dataFields: [
        {name: "id", type: "number"},
        {name: "title", type: "string"},
        {name: "parentId", type: "number"},
        {name: "children", type: "array"},
        {name: "expanded", type: "bool"}
      ],
      hierarchy: {
        root: "children"
      },
      id: "id",
      localData: this.tags
    };
  }

  dataAdapter: any;

  columns =
    [
      {text: 'KnowNet tags base', dataField: 'title', minWidth: 100, width: this.getWidth()},
    ];

  ready = (): void => {
    this.myTreeGrid.expandRow(32);
    document.addEventListener('contextmenu', event => event.preventDefault());
  };

  editSettings: any =
    {
      saveOnPageChange: true, saveOnBlur: true, saveOnSelectionChange: true,
      cancelOnEsc: true, saveOnEnter: true, editSingleCell: true, editOnDoubleClick: true, editOnF2: true
    };

  oldCellName: string;
  
  treeGridOnCellBeginEdit(event: any): void {
    this.oldCellName = event.args.value; 
    console.log("editing tag \'" + this.oldCellName + "\'");
    console.log(event.args);
  }

  treeGridOnCellEndEdit(event: any): void {
    let args = event.args;
    let rowKey = args.key; // id of tag
    let rowData = args.row; // all internal data of tag
    let value = args.value; // title of selected tag
    
    console.log(value);
    
    if (this.oldCellName === value || value == "") {
      console.log("no changes");
      return;
    }
    

      this.tagService.getById(rowKey).subscribe(tag => {
        tag.title = value;
        this.tagService.update(tag).subscribe(_ => {
          console.log("item id=" + tag.id + " \'" +  this.oldCellName + "\' was renamed, new name is \'" + value + "\'");
        });
      });

  }
  

  myTreeGridOnRowClick(event: any): boolean {
    let args = event.args;
    if (args.originalEvent.button == 2) {
      let scrollTop = window.scrollX;
      let scrollLeft = window.scrollY;
      this.myMenu.open(parseInt(event.args.originalEvent.clientX) + 5 + scrollLeft, parseInt(event.args.originalEvent.clientY) + 5 + scrollTop);
      return false;
    }
  };

  tagNumbers: number[];
  
  deleteChild(array: any[]): void {
    for (let i = 0; i < array.length; i++) {
      this.tagService.delete(array[i].id).subscribe();
      if (array[i].children.length == 0)
        return;
      this.deleteChild(array[i].children);
    }
  }
  
  myMenuOnItemClick(event: any): void {
    let args = event.args;
    let selection = this.myTreeGrid.getSelection();
    let rowid = selection[0].uid;

    switch (args.innerHTML) { 
      case 'Delete Selected Row':
        // todo Нельзя удалить тег, id которого используется в каком-нибудь реквесте. Нужно выводить сообщение о том,
        // todo  что этот тег используется
        
        let selection = this.myTreeGrid.getSelection();
        
        if (selection.length > 1) {
          for (let i = 0; i < selection.length; i++) {
            let key = this.myTreeGrid.getKey(selection[i]);
            this.myTreeGrid.deleteRow(key);
          }
        }
        else {
          this.myTreeGrid.deleteRow(rowid);
        }

        this.deleteChild(selection);
        
        break;
      case 'Add new Row':
        let tag: Tag = new Tag();
        tag.parentId = rowid;
        tag.title = "TAGCREATED";
        this.tagService.create(tag).subscribe(_=> {
          this.tagService.getLastId().subscribe( value => {
            this.myTreeGrid.expandRow(rowid);
            this.myTreeGrid.addRow(value, {}, 'first', rowid);
            this.myTreeGrid.clearSelection();
            this.myTreeGrid.selectRow(value);
            this.myTreeGrid.beginRowEdit(value);
          });
        });
        break;
    }
  };

  expandAll(): void {
    this.myTreeGrid.expandAll();
  }

  collapseAll(): void {
    this.myTreeGrid.collapseAll();
  }

  addRootTag(): void {
    let tag: Tag = new Tag();
    tag.parentId = 0;
    tag.title = "ROOTCREATED";
    this.tagService.create(tag).subscribe(_=>{
      this.tagService.getLastId().subscribe( value => {
        this.myTreeGrid.addRow(value, {}, 'first');
        this.myTreeGrid.clearSelection();
        this.myTreeGrid.selectRow(value);
      });
    });
  }
}
