Set objFSO = CreateObject("Scripting.FileSystemObject")
Set colDrives = objFSO.Drives
Set objDrive = colDrives.item("C")
Wscript.Echo objDrive.SerialNumber